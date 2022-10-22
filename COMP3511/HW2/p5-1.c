#include <stdio.h>
#include <string.h>
#include <unistd.h>
#include <sys/wait.h>
int main() {
 char msg[50] = "empty message";
 pid_t pid = fork();
 if ( pid > 0 ) { // parent
 wait(0); // wait for the child
 printf("The message in parent is %s\n", msg);
 } else { // child
 strcpy(msg, "secret message");
 printf("The message in child is %s\n", msg);
 }
 return 0;
}

// Output:
// The message in child is secret message
// The message in parent is empty message