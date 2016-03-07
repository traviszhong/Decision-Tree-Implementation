package umt.ml.ID3;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;



/**
 * 
 * @author Zhong Ziyue
 *
 * @email zhongzy@strongit.com.cn
 * 
 * Mar 6, 2016
 */
public class Test {
	private int[][] testData;
	private int[] classLabel;
	private String dataPath;
	private Classifier c;
	public Test(String path) throws Exception{
		this.dataPath=path;
		this.testData=new int[70][9];
		this.classLabel=new int[70];
		this.c=new Classifier("src/train.csv");
		loadData();
	}
	public void loadData() throws Exception{
		File csvData= new File(this.dataPath);
		BufferedReader br=new BufferedReader(new FileReader(csvData));
		String line="";
		int index=-1;
		while((line=br.readLine())!=null){
			if(index==-1){
				index++;continue;
			}
			StringTokenizer st=new StringTokenizer(line,",");
			for(int i=0;i<=8;i++){
				testData[index][i]=Integer.valueOf(st.nextToken());
			}
			classLabel[index]=Integer.parseInt(st.nextToken());
			index++;
		}
	}
	public double testAccuracy() throws Exception{
		int count=0;
		for(int i=0;i<70;i++){
			List<Integer> sample=new ArrayList<Integer>();
			for(int j=0;j<9;j++) sample.add(this.testData[i][j]);
			int rst=this.c.getClassLabel(sample);
			if(rst==this.classLabel[i]) count++;
		}
		return count/70.0;
	}
}
