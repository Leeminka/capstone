package cs.kookmin.ac.kr;

import java.io.File;
import java.io.IOException;

public class FindCache {


	public static void main(String[] args)
	{
		subDirList("C:\\Users/2S/Desktop/�ְ�/958745313");
	}
   
	public static void subDirList(String source){
		File dir = new File(source); 
		File[] fileList = dir.listFiles(); 
		try{
			for(int i = 0 ; i < fileList.length ; i++){
				File file = fileList[i]; 
				if(file.isFile()){
    // ������ �ִٸ� ���� �̸� ���
					if(file.length() > 409600)
						System.out.println("\t ���� ��� = " + file.getPath());
				}else if(file.isDirectory()){
					//System.out.println("���丮 �̸� = " + file.getName());
    // ������丮�� �����ϸ� ����� ������� �ٽ� Ž��
					subDirList(file.getCanonicalPath().toString()); 
				}
			}
		}catch(IOException e){
			
		}
	}
}
