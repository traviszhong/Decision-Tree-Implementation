package umt.ml.ID3;


public class RunItHere {


	public static void main(String[] args) {
		// TODO Auto-generated method stub
		try {
			Test t=new Test("src/test.csv");
			System.out.println(t.testAccuracy());
			/*for serialize the decision tree*/
			Classifier c=new Classifier("src/train.csv");
			System.out.print(c.bfsTraversal());
		} catch (Exception e) {

			e.printStackTrace();
		}
		
	}

}
