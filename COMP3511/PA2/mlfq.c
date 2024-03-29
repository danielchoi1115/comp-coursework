/*
    COMP3511 Fall 2022 
    PA2: Simplified Multi-Level Feedback Queue (MLFQ)

    Your name: CHOI, Seung-ryeol
    Your ITSC email:    schoiak@connect.ust.hk 

    Declaration:

    I declare that I am not involved in plagiarism
    I understand that both parties (i.e., students providing the codes and students copying the codes) will receive 0 marks. 

*/

// Note: Necessary header files are included
#define _GNU_SOURCE
#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <ctype.h>

// Define MAX_NUM_PROCESS
// For simplicity, assume that we have at most 10 processes

#define MAX_NUM_PROCESS 10
#define MAX_QUEUE_SIZE 10
#define MAX_PROCESS_NAME 5
#define MAX_GANTT_CHART 300

// Keywords (to be used when parsing the input)
#define KEYWORD_TQ0 "tq0"
#define KEYWORD_TQ1 "tq1"
#define KEYWORD_PROCESS_TABLE_SIZE "process_table_size"
#define KEYWORD_PROCESS_TABLE "process_table"

// Assume that we only need to support 2 types of space characters: 
// " " (space), "\t" (tab)
#define SPACE_CHARS " \t"

// Process data structure
// Helper functions:
//  process_init: initialize a process entry
//  process_table_print: Display the process table
struct Process {
    char name[MAX_PROCESS_NAME];
    int arrival_time ;
    int burst_time;
    int remain_time; // remain_time is needed in the intermediate steps of MLFQ 
};
void process_init(struct Process* p, char name[MAX_PROCESS_NAME], int arrival_time, int burst_time) {
    strcpy(p->name, name);
    p->arrival_time = arrival_time;
    p->burst_time = burst_time;
    p->remain_time = 0;
}
void process_table_print(struct Process* p, int size) {
    int i;
    printf("Process\tArrival\tBurst\n");
    for (i=0; i<size; i++) {
        printf("%s\t%d\t%d\n", p[i].name, p[i].arrival_time, p[i].burst_time);
    }
}

// A simple integer queue implementation using a fixed-size array
// Helper functions:
//   queue_init: initialize the queue
//   queue_is_empty: return true if the queue is empty, otherwise false
//   queue_is_full: return true if the queue is full, otherwise false
//   queue_peek: return the current front element of the queue
//   queue_enqueue: insert one item at the end of the queue
//   queue_dequeue: remove one item from the beginning of the queue
//   queue_print: display the queue content, it is useful for debugging
struct Queue {
    int values[MAX_QUEUE_SIZE];
    int front, rear, count;
};
void queue_init(struct Queue* q) {
    q->count = 0;
    q->front = 0;
    q->rear = -1;
}
int queue_is_empty(struct Queue* q) {
    return q->count == 0;
}
int queue_is_full(struct Queue* q) {
    return q->count == MAX_QUEUE_SIZE;
}

int queue_peek(struct Queue* q) {
    return q->values[q->front];
}
void queue_enqueue(struct Queue* q, int new_value) {
    if (!queue_is_full(q)) {
        if ( q->rear == MAX_QUEUE_SIZE -1)
            q->rear = -1;
        q->values[++q->rear] = new_value;
        q->count++;
    }
}
void queue_dequeue(struct Queue* q) {
    q->front++;
    if (q->front == MAX_QUEUE_SIZE)
        q->front = 0;
    q->count--;
}
void queue_print(struct Queue* q) {
    int c = q->count;
    printf("size = %d\n", c);
    int cur = q->front;
    printf("values = ");
    while ( c > 0 ) {
        if ( cur == MAX_QUEUE_SIZE )
            cur = 0;
        printf("%d ", q->values[cur]);
        cur++;
        c--;
    }
    printf("\n");
}

// A simple GanttChart structure
// Helper functions:
//   gantt_chart_update: append one item to the end of the chart (or update the last item if the new item is the same as the last item)
//   gantt_chart_print: display the current chart
struct GanttChartItem {
    char name[MAX_PROCESS_NAME];
    int duration;
};
void gantt_chart_update(struct GanttChartItem chart[MAX_GANTT_CHART], int* n, char name[MAX_PROCESS_NAME], int duration) {
    int i;
    i = *n;
    // The new item is the same as the last item
    if ( i > 0 && strcmp(chart[i-1].name, name) == 0) 
    {
        chart[i-1].duration += duration; // update duration
    } 
    else
    {
        strcpy(chart[i].name, name);
        chart[i].duration = duration;
        *n = i+1;
    }
}
void gantt_chart_print(struct GanttChartItem chart[MAX_GANTT_CHART], int n) {
    int t = 0;
    int i = 0;
    printf("Gantt Chart = ");
    printf("%d ", t);
    for (i=0; i<n; i++) {
        t = t + chart[i].duration;     
        printf("%s %d ", chart[i].name, t);
    }
    printf("\n");
}

// Global variables
int tq0 = 0, tq1 = 0;
int process_table_size = 0;
struct Process process_table[MAX_NUM_PROCESS];



// Helper function: Check whether the line is a blank line (for input parsing)
int is_blank(char *line) {
    char *ch = line;
    while ( *ch != '\0' ) {
        if ( !isspace(*ch) )
            return 0;
        ch++;
    }
    return 1;
}
// Helper function: Check whether the input line should be skipped
int is_skip(char *line) {
    if ( is_blank(line) )
        return 1;
    char *ch = line ;
    while ( *ch != '\0' ) {
        if ( !isspace(*ch) && *ch == '#')
            return 1;
        ch++;
    }
    return 0;
}
// Helper: parse_tokens function
void parse_tokens(char **argv, char *line, int *numTokens, char *delimiter) {
    int argc = 0;
    char *token = strtok(line, delimiter);
    while (token != NULL)
    {
        argv[argc++] = token;
        token = strtok(NULL, delimiter);
    }
    *numTokens = argc;
}

// Helper: parse the input file
void parse_input() {
    FILE *fp = stdin;
    char *line = NULL;
    ssize_t nread;
    size_t len = 0;

    char *two_tokens[2]; // buffer for 2 tokens
    char *three_tokens[3]; // buffer for 3 tokens
    int numTokens = 0, n=0, i=0;
    char equal_plus_spaces_delimiters[5] = "";

    char process_name[MAX_PROCESS_NAME];
    int process_arrival_time = 0;
    int process_burst_time = 0;

    strcpy(equal_plus_spaces_delimiters, "=");
    strcat(equal_plus_spaces_delimiters,SPACE_CHARS);

    while ( (nread = getline(&line, &len, fp)) != -1 ) {
        if ( is_skip(line) == 0)  {
            line = strtok(line,"\n");

            if (strstr(line, KEYWORD_TQ0)) {
                // parse tq0
                parse_tokens(two_tokens, line, &numTokens, equal_plus_spaces_delimiters);
                if (numTokens == 2) {
                    sscanf(two_tokens[1], "%d", &tq0);
                }
            } 
            else if (strstr(line, KEYWORD_TQ1)) {
                // parse tq0
                parse_tokens(two_tokens, line, &numTokens, equal_plus_spaces_delimiters);
                if (numTokens == 2) {
                    sscanf(two_tokens[1], "%d", &tq1);
                }
            }
            else if (strstr(line, KEYWORD_PROCESS_TABLE_SIZE)) {
                // parse process_table_size
                parse_tokens(two_tokens, line, &numTokens, equal_plus_spaces_delimiters);
                if (numTokens == 2) {
                    sscanf(two_tokens[1], "%d", &process_table_size);
                }
            } 
            else if (strstr(line, KEYWORD_PROCESS_TABLE)) {

                // parse process_table
                for (i=0; i<process_table_size; i++) {

                    getline(&line, &len, fp);
                    line = strtok(line,"\n");  

                    sscanf(line, "%s %d %d", process_name, &process_arrival_time, &process_burst_time);
                    process_init(&process_table[i], process_name, process_arrival_time, process_burst_time);

                }
            }

        }
        
    }
}
// Helper: Display the parsed values
void print_parsed_values() {
    printf("%s = %d\n", KEYWORD_TQ0, tq0);
    printf("%s = %d\n", KEYWORD_TQ1, tq1);
    printf("%s = \n", KEYWORD_PROCESS_TABLE);
    process_table_print(process_table, process_table_size);
}

// Custom Helper: returns total burst time
int get_total_time(struct Process* p, int size){
    int t = 0;
    int i;
    for (i=0; i<size; i++) {
        t = t + p[i].burst_time;
    }
    return t;
}
// Custom Helper: returns process name if arrived at specific time, else -1
int has_arrived(int t){
    int i;
    for (i=0; i<process_table_size; i++) {
        if (t == process_table[i].arrival_time){
            return i;
        }
    }
    return -1;
}
// Custom Helper: set a process remain time to quantum
void set_quantum(int pid, int quantum){
    process_table[pid].remain_time = quantum;
}
// Custom Helper: Deduct burst_time and remain_time by 1 time unit
void burst_process(int pid){
    process_table[pid].remain_time -= 1;
    process_table[pid].burst_time -= 1;
}
// Custom Helper: returns True if a process has no quantum else False
int has_no_quantum(int pid){
    return process_table[pid].remain_time == 0;
}
// Custom Helper: returns True if a process has burst time else False
int has_burst_time(int pid){
    return process_table[pid].burst_time > 0;
}
// Custom Helper: if process still has burst time but no quantum, move it to the next queue
void move_to_next_queue(struct Queue* from, struct Queue* to, int pid, int new_quantum){
    if (new_quantum == 0) {
        new_quantum = process_table[pid].burst_time;
    }
    queue_dequeue(from);
    set_quantum(pid, new_quantum);
    queue_enqueue(to, pid);
}

void mlfq() {

    // Initialize the gantt chart
    struct GanttChartItem chart[MAX_GANTT_CHART];
    int sz_chart = 0;
    int t = 0;
    int total_time = get_total_time(process_table, process_table_size);

    // TODO: implement your MLFQ algorithm here
    // Initialize queues
    struct Queue q0;
    queue_init(&q0);
    struct Queue q1;
    queue_init(&q1);
    struct Queue fifo;
    queue_init(&fifo);

    while (t < total_time){
        int arrived_pid = -1;
        
        arrived_pid = has_arrived(t);
        if (arrived_pid >= 0) {
            set_quantum(arrived_pid, tq0);
            queue_enqueue(&q0, arrived_pid);
        }
        
        struct Queue* curr_q;
        struct Queue* next_q;
        int quantum;

        if (queue_is_empty(&q0) == 0) {
            curr_q = &q0;
            next_q = &q1;
            quantum = tq1;
        }
        else if (queue_is_empty(&q1) == 0) 
        {
            curr_q = &q1;
            next_q = &fifo;
            quantum = 0;
        }
        else if (queue_is_empty(&fifo) == 0){
            curr_q = &fifo;
            next_q = NULL;
            quantum = 0;
        }
        // if nothing to proceed, continue
        else {
            t += 1;
            continue;
        }

        int pid = queue_peek(curr_q);
        // deduct burst_time and remain_time by 1 time unit
        burst_process(pid);
        // if process still has burst time but no quantum, move it to the next queue
        if (has_no_quantum(pid) && has_burst_time(pid)) {
            move_to_next_queue(curr_q, next_q, pid, quantum);
        }
        else if (has_burst_time(pid) == 0) {
            queue_dequeue(curr_q);
        }
        gantt_chart_update(chart, &sz_chart, process_table[pid].name, 1);
        t += 1;
    }
    gantt_chart_print(chart, sz_chart);
}


int main() {
    parse_input();
    print_parsed_values();
    mlfq();
    return 0;
}