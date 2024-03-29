#include <stdio.h>
#include <string.h>
#include <unistd.h>
#include <sys/wait.h>
int main() {
 char msg[50] = "empty message";
 const char secret_msg[] = "secret message"; 
 int secret_length = strlen(secret_msg);
 int pfds[2];

 pipe(pfds);
 
 pid_t pid = fork();
 if ( pid > 0 ) { // parent
 wait(0); // wait for the child

 close(pfds[1]);
 
 read(pfds[0], msg, strlen(secret_msg));

 printf("The message in parent is %s\n", msg);
 } else { // child
 strcpy(msg, secret_msg);
 
 close(pfds[0]);
 
 write(pfds[1], msg, strlen(secret_msg));

 printf("The message in child is %s\n", msg);
 }
 return 0;
}