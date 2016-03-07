package umt.ml.ID3;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
/**
 * 
 * @author Zhong Ziyue
 *
 * @email zhongzy@strongit.com.cn
 * 
 * Mar 6, 2016
 */
public class DecisionTreeNode {
	int classLable;
	int dimId;
	List<Integer> dimList;
	List<Integer> sampleList;
	Map<Integer,DecisionTreeNode> next;
	public DecisionTreeNode(List<Integer> dim,List<Integer> sample){
		this.dimList=dim;
		this.sampleList=sample;
		this.next=new HashMap<Integer,DecisionTreeNode>();
		this.classLable=2;
		this.dimId=-1;
	}
}
