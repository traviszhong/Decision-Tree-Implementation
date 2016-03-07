package umt.ml.ID3;
/**
 * 
 * @author Zhong Ziyue
 *
 * @email zhongzy@strongit.com.cn
 * 
 * Mar 6, 2016
 */
public class Qnode {
	int parent;
	DecisionTreeNode n;
	public Qnode(int p,DecisionTreeNode node){this.parent=p;this.n=node;}
}
