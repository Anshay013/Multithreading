package Main1;

 // Threads allows a program to operate more efficiently by doing multiple things at the same time. Threads can be used to perform complicated tasks in the
 // background without interrupting the main program.
public class Threads {
    public static void main(String[] args){
        System.out.println("I am writing in " + Thread.currentThread().getName() + "thread");
        

        MyThread thread = new MyThread(); // creating object of thread.
        thread.start();   

        System.out.println("main function has done all the work");
    }


    public static  class MyThread extends Thread {  
        @Override
        public void run() {
            System.out.println("I am writing in " + currentThread().getName() + "thread");
            try {
                Thread.sleep(10000); 
                throw new InterruptedException("print any msg of your choice or can leave it as blank"); 
            } catch (InternalError e) { 
                System.out.println("I am writing in " + Thread.currentThread().getName() + "Thread");
            }
            catch (Exception e) { 
                System.out.println("Some error encountered");
            }
          
            finally {
            System.out.println("whatever happens this statement will always be printed");
            }
            }

        }
    }


