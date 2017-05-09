package wb;

import junit.framework.*;

public class UnitTests extends TestCase {
    private View v;
    private Controller c;
    private Board b;
   // assigning the values
   protected void setUp(){
      v = new View();
      c = new Controller();
      b = new Board();
   }

   // test method to add two values
   public void testAdd(){
      double result = 3 + 3;
      assertTrue(result == 6);
   }

   public void testBoard() {
       return;
   }

   public void testView() {
       return;
   }
}
