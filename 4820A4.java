import java.io.*;
import java.util.*;

class Main {

    public static void main(String[] args) throws Exception {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        String line = br.readLine();
        StringTokenizer st = new StringTokenizer(line);

        int rows = Integer.parseInt(st.nextToken());
        int columns = Integer.parseInt(st.nextToken());

        int[][] pPrefixSum = new int[rows][columns];
        int[][] hPrefixSum = new int[rows][columns];

        int pointer = 0;
        for(int i = 0; i < rows; i++){
            String currentLine = br.readLine();
            for(int j = 0; j < columns; j++){
                char currentChar = currentLine.charAt(pointer);
                int sump = 0;
                int sumh = 0;
                if(i - 1 >= 0 && j - 1 >= 0){
                    sump = pPrefixSum[i-1][j] + pPrefixSum[i][j-1] - pPrefixSum[i-1][j-1];
                    sumh = hPrefixSum[i-1][j] + hPrefixSum[i][j-1] - hPrefixSum[i-1][j-1];
                }
                if(i - 1 >= 0 && j - 1 < 0){
                    sump = pPrefixSum[i-1][j];
                    sumh = hPrefixSum[i-1][j];
                }
                if(i - 1 < 0 && j - 1 >= 0){
                    sump = pPrefixSum[i][j-1];
                    sumh = hPrefixSum[i][j-1];
                }
                if(currentChar == 'p'){
                    pPrefixSum[i][j] = sump + 1;
                    hPrefixSum[i][j] = sumh;
                }else if(currentChar == 'h'){
                    pPrefixSum[i][j] = sump;
                    hPrefixSum[i][j] = sumh + 1;
                }else{
                    pPrefixSum[i][j] = sump;
                    hPrefixSum[i][j] = sumh;
                }
                pointer++;
            }
            pointer = 0;
        }
    }
}
