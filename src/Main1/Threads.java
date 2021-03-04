package Main1;

 // Threads allows a program to operate more efficiently by doing multiple things at the same time. Threads can be used to perform complicated tasks in the
 // background without interrupting the main program.
public class Threads {
    public static void main(String[] args){
        System.out.println("I am writing in " + Thread.currentThread().getName() + "thread"); // main thread.
         // note this main thread and the fun called under run executes parallel to each other since threads are used different call stacks for diff.
        // fun are made .

        MyThread thread = new MyThread(); // creating object of thread.
        thread.start();   // creates a new thread.(start is again an inbuilt fun. of Thread.java it returns void)
                          // start fun has an run fun. connected somewhere to it hence, run fun inside MtThread is called.

        System.out.println("main function has done all the work");
    }


    public static  class MyThread extends Thread {  // --> OR  public static class implements Runnable ( either extend to thread class or implement it to
                                                        // Runnable interface.
        // this fun. tells how the thread will perform (this Thread class come pre-bundled  with jdk we could
                                                    // do it into main class as well but we want to make a cleaner code design
        @Override
        public void run() {
            System.out.println("I am writing in " + currentThread().getName() + "thread");// currentThread returns Thread object. currentThread is
            // basically the current given thread again this currentThread is present in Thread.java( Thread class) we call this fun. and compiler will
            // look into existing class(run class) if this currentThread fun is present or not if not the it will look into the parent class i.e Thread.java
            // (getName returns string name of thread it is also present in Thread.java)
            try {
                Thread.sleep(10000); // we stop the execution for given time (milli sec).(1000 millis = 1 sec)
                throw new InterruptedException("print any msg of your choice or can leave it as blank"); // we throw an interrupted exc. e
            } catch (InternalError e) { // now catch the thrown  exc. e(here catch will only catch the exc. of same type passed in it otherwise it wont )
                // if we catch say InternalException in here and exc. thrown was interrupted the print statement under catch wont be printed.
           //     e.printStackTrace();
                System.out.println("I am writing in " + Thread.currentThread().getName() + "Thread");
            }
            catch (Exception e) { // we catch here the normal exc.,normal can InterruptedException as well in other words it can catch every type of exc.
                // (i.e any other exception occurs along with other than interrupted exc. ) say if the only exc. was the interruptedException then this
                // catch will not catch any exc. because their is no exc. to catch( exc .already been caught in above catch).
                // *note -> things under catch will only execute if it catch  catches some exception.( more likely if their catch block will only be called
                // if their is some exc.)
                System.out.println("Some error encountered");
            }
            // above catch catches all types of exc. now if we want to introduce new catch,we get an error because above catch already caught every
            // type of exc. possible so their will be no exc. to be caught resulting in error display.
            // therefore always make sure if parameter inside some catch is catch(Exception e) then new catch after it cannot be declared.(so always add
            // this catch(Exception e at the last if we want to declare multiple catches).
            finally {
            System.out.println("whatever happens this statement will always be printed");
            }
            }

        }
    }


