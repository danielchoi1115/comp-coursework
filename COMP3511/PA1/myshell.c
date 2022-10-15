/*
    COMP3511 Fall 2022 
    PA1: Simplified Linux Shell (MyShell)

    Your name: CHOI Seung-ryeol
    Your ITSC email:   schoiak@connect.ust.hk 

    Declaration:

    I declare that I am not involved in plagiarism
    I understand that both parties (i.e., students providing the codes and students copying the codes) will receive 0 marks. 

*/

// Note: Necessary header files are included
#define _GNU_SOURCE
#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <unistd.h>
#include <sys/types.h> 
#include <sys/stat.h> // For constants that are required in open/read/write/close syscalls
#include <sys/wait.h> // For wait() - suppress warning messages
#include <fcntl.h> // For open/read/write/close syscalls

// Assume that each command line has at most 256 characters (including NULL)
#define MAX_CMDLINE_LEN 256

// Assume that we have at most 8 arguments
#define MAX_ARGUMENTS 8

// Assume that we only need to support 2 types of space characters: 
// " " (space) and "\t" (tab)
#define SPACE_CHARS " \t"

// The pipe character
#define PIPE_CHAR "|"

// Assume that we only have at most 8 pipe segements, 
// and each segment has at most 256 characters
#define MAX_PIPE_SEGMENTS 8

// Assume that we have at most 8 arguments for each segment
//
// We also need to add an extra NULL item to be used in execvp
//
// Thus: 8 + 1 = 9
//
// Example: 
//   echo a1 a2 a3 a4 a5 a6 a7 
//
// execvp system call needs to store an extra NULL to represent the end of the parameter list
//
//   char *arguments[MAX_ARGUMENTS_PER_SEGMENT]; 
//
//   strings stored in the array: echo a1 a2 a3 a4 a5 a6 a7 NULL
//
#define MAX_ARGUMENTS_PER_SEGMENT 9

// Define the  Standard file descriptors here
#define STDIN_FILENO    0       // Standard input
#define STDOUT_FILENO   1       // Standard output 


 
// This function will be invoked by main()
// TODO: Implement the multi-level pipes below
void process_cmd(char *cmdline);

// read_tokens function is given
// This function helps you parse the command line
// Note: Before calling execvp, please remember to add NULL as the last item 
void read_tokens(char **argv, char *line, int *numTokens, char *token);

// Here is an example code that illustrates how to use the read_tokens function
// int main() {
//     char *pipe_segments[MAX_PIPE_SEGMENTS]; // character array buffer to store the pipe segements
//     int num_pipe_segments; // an output integer to store the number of pipe segment parsed by this function
//     char cmdline[MAX_CMDLINE_LEN]; // the input argument of the process_cmd function
//     int i, j;
//     char *arguments[MAX_ARGUMENTS_PER_SEGMENT] = {NULL}; 
//     int num_arguments;
//     strcpy(cmdline, "ls | sort -r | sort | sort -r | sort | sort -r | sort | sort -r");
//     read_tokens(pipe_segments, cmdline, &num_pipe_segments, PIPE_CHAR);
//     for (i=0; i< num_pipe_segments; i++) {
//         printf("%d : %s\n", i, pipe_segments[i] );    
//         read_tokens(arguments, pipe_segments[i], &num_arguments, SPACE_CHARS);
//         for (j=0; j<num_arguments; j++) {
//             printf("\t%d : %s\n", j, arguments[j]);
//         }
//     }
//     return 0;
// }

void run_cmd(char **argv, char *sin, char *sout);

/* The main function implementation */
int main()
{   

    char cmdline[MAX_CMDLINE_LEN];
    fgets(cmdline, MAX_CMDLINE_LEN, stdin);
    process_cmd(cmdline);
    return 0;
}
void exec_cmd(char **argv, int argn){
    int j;
    char *arg_temp[MAX_ARGUMENTS_PER_SEGMENT] = {NULL}; 
    char *std_in = NULL;
    char *std_out = NULL;
    // Extract cmd part
    for (j=0; j<argn; j++) {
        if (!strcmp(argv[j], "<") || !strcmp(argv[j], ">")){
            break;
        }
        arg_temp[j] = argv[j];
    } 
    // extract input and output parts
    for (j=0; j<argn; j++) {
        if (!strcmp(argv[j], "<")) {
            std_in = argv[j+1];
        }
        else if (!strcmp(argv[j], ">")) {
            std_out = argv[j+1];
        }
    }

    if (std_out) {
        int fd_out;
        fd_out = open(std_out, /* output file name */
        O_CREAT | O_WRONLY , /* flags */
        S_IRUSR | S_IWUSR ); /* user permission: 600 */
        close(1); /* Close stdout */
        dup(fd_out); /* Replace stdout using the new file descriptor ID */
        fflush(stdout); /* ensure all characters are output from the buffer */
    }
    if (std_in) {
        int fd_in; 
        fd_in = open(std_in, /* output file name */
        O_CREAT | O_RDONLY , /* flags */
        S_IRUSR | S_IWUSR );
        close(0); /* close stdin */
        dup(fd_in); /* make stdin as pipe output */
    }
    execvp(arg_temp[0], arg_temp);
}

// TODO: implementation of process_cmd
// https://stackoverflow.com/questions/17630247/coding-multiple-pipe-in-c
void process_cmd(char *cmdline)
{

    char *pipe_segments[MAX_PIPE_SEGMENTS]; // character array buffer to store the pipe segements
    int num_pipe_segments; // an output integer to store the number of pipe segment parsed by this function

    int i;
    char *arguments[MAX_ARGUMENTS_PER_SEGMENT] = {NULL}; 
    int num_arguments;
    read_tokens(pipe_segments, cmdline, &num_pipe_segments, PIPE_CHAR);
    int p[2];
    int pfd_in = 0;
    pid_t pid;
    for (i=0; i<num_pipe_segments; i++) {
        pipe(p);
        if ((pid = fork()) == 0) {
            dup2(pfd_in, 0);
            if (i+1 < num_pipe_segments) { // If not the last one, make stdout as pipe in
                dup2(p[1], 1);
            }
            close(p[0]);
            read_tokens(arguments, pipe_segments[i], &num_arguments, SPACE_CHARS);
            exec_cmd(arguments, num_arguments);
            exit(EXIT_FAILURE);
            
        }
        else {
            wait(NULL);
            close(p[1]);
            pfd_in = p[0];
        }
    }
    return;
}

// Implementation of read_tokens function
void read_tokens(char **argv, char *line, int *numTokens, char *delimiter)
{
    int argc = 0;
    char *token = strtok(line, delimiter);
    while (token != NULL)
    {
        argv[argc++] = token;
        token = strtok(NULL, delimiter);
    }
    *numTokens = argc;
}

