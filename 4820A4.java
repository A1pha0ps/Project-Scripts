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

        int[][][][] dp = new int[rows][rows][columns][columns];

        for(int deltay = 0; deltay < rows; deltay++){
            for(int deltax = 0; deltax < columns; deltax++){
                for(int y = 0; y < rows - deltay; y++){
                    for(int x = 0; x < columns - deltax; x++) {
                        //System.out.println(y + "," + deltay + "," + x + "," + deltax);
                        boolean hasOne = false;
                        if (x == 0 && y == 0 && deltax == 0 && deltay == 0) {
                            hasOne = true;
                        } else if (y == 0) {
                            int area = deltax + 1;
                            int n = pPrefixSum[0][x + deltax];
                            int m = hPrefixSum[0][x + deltax];
                            if (x - 1 >= 0) {
                                n -= pPrefixSum[0][x - 1];
                                m -= hPrefixSum[0][x - 1];
                            }
                            System.out.println(area + "," + n + "," + m);
                            if (n == area || m == area || (n == 0 && m == 0)) {
                                hasOne = true;
                            }else{
                                dp[y][y + deltay][x][x + deltax] = Integer.MAX_VALUE;
                            }
                        } else if (x == 0) {
                            int area = deltay + 1;
                            int n = pPrefixSum[y + deltay][0];
                            int m = hPrefixSum[y + deltay][0];
                            if (y - 1 >= 0) {
                                n -= pPrefixSum[y - 1][0];
                                m -= hPrefixSum[y - 1][0];
                            }
                            if (n == area || m == area || (n == 0 && m == 0)) {
                                hasOne = true;
                            }else{
                                dp[y][y + deltay][x][x + deltax] = Integer.MAX_VALUE;
                            }
                        } else {
                            int area = (deltax + 1) * (deltay + 1);
                            int n = pPrefixSum[y + deltay][x + deltax] - pPrefixSum[y + deltay][x - 1] - pPrefixSum[y - 1][x + deltax] + pPrefixSum[y - 1][x - 1];
                            int m = hPrefixSum[y + deltay][x + deltax] - hPrefixSum[y + deltay][x - 1] - hPrefixSum[y - 1][x + deltax] + hPrefixSum[y - 1][x - 1];
                            if (n == area || m == area || (n == 0 && m == 0)) {
                                hasOne = true;
                            }else{
                                dp[y][y + deltay][x][x + deltax] = Integer.MAX_VALUE;
                            }
                        }

                        if(hasOne){
                            dp[y][y + deltay][x][x + deltax] = 0;
                        }else{
                            int bestVerticalCut = Integer.MAX_VALUE;
                            for(int i = x; i < x + deltax; i++){
                                int possible1 = 1 + dp[y][y + deltay][x][i] + dp[y][y + deltay][i+1][deltax + x];
                                bestVerticalCut = Math.min(bestVerticalCut,possible1);
                            }
                            int bestHorizontalCut = Integer.MAX_VALUE;
                            for(int j = y; j < y + deltay; j++){
                                int possible2 = 1 + dp[y][j][x][x + deltax] + dp[j+1][y + deltay][x][deltax + x];
                                bestHorizontalCut = Math.min(bestHorizontalCut,possible2);
                            }

                            dp[y][y + deltay][x][x + deltax] = Math.min(bestHorizontalCut, bestVerticalCut);
                        }

                        System.out.println("y: " + y + ", deltay: " + deltay + ", x: " + x + ", deltax: " + deltax + ", hasOne: " + hasOne + " ; " + dp[y][y + deltay][x][x+deltax]);
                    }
                }
            }
        }
    }

}
