package wb;

class Controller {

    private View v;
    private Model m;

    public Controller(){

    }

    public void makeModel(String filePath){
    	m = new Model(filePath);
    }

}
