package wb;

class Controller {

    private View v;
    private Model m;

    public Controller(){
        v = new View();
        m = new Model();
        // this.makeModel(filePath)
    }

    public void makeModel(String filePath){
    	m = new Model(filePath);
    }

    /// this is pseudo java
    public void run() {
        while (true) {
            if (gameOver) { // check current games state
                break;
            }
            for (Event e : View) { // this is pseudo code
                // update game board.
                if (processEvent(e)) {
                    v.render();
                }
                if (gameOver) {
                    break;
                }
            }
        }
    }

    // What's the view said has just happened?
    // Update model to new state.
    // directions in clockwise form.
    // 0, 1, 2, 3 corresponding UP, RIGHT, DOWN, LEFT
    private boolean processEvent(Event e) {
        switch (e.getKeyCode()) {
            boolean updateMade = false;
            case (KeyEvent.VK_KP_UP):
                updateMade = m.makeMove(0);
                break;
            case (KeyEvent.VK_KP_RIGHT):
                updateMade = m.makeMove(1);
                break;
            case (KeyEvent.VK_KP_DOWN):
                updateMade = m.makeMove(2);
                break;
            case (KeyEvent.VK_KP_LEFT):
                updateMade = m.makeMove(3);
                break;
            default:
                break;
        }
        return updateMade;
    }


}
