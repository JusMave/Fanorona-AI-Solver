import java.util.HashSet;
import java.util.Set;

public class Piece {
    int y;
    int x;
    int color;
    int opponent;
    int[][] boardData;
    Piece[][] pieces;
    Set<Set<int[]>> captureNum = new HashSet<>();
    Set<Set<int[]>> retreatNum = new HashSet<>();
    Set<int[]> empties = new HashSet<>();

    Piece(int positionY, int positionX, int player, int[][] board, Piece[][] allPieces){
        y = positionY;
        x = positionX;
        color = player;
        boardData = board;
        pieces = allPieces;
        if(player == 0){
            opponent = 1;
        } else if(player == 1) {
            opponent = 0;
        }
    }

    public void checkMovable(int[] empty){
        //System.out.println("empty: (" + empty[0] + ", " + empty[1] + ")");
        //System.out.println("(" + y + ", " + x + ")");
        int emptyX = empty[1];
        int emptyY = empty[0];
        int differX = emptyX - x;
        int differY = emptyY - y;
        /*
        int checkPieceNum;
        int tempX, tempY;
        if(differX <= 0){
            tempX = -differX;
        } else {
            tempX = differX;
        }
        if(differY <= 0){
            tempY = -differY;
        } else {
            tempY = differY;
        }
        if(tempX >= tempY){
            checkPieceNum = tempX;
        } else {
            checkPieceNum = tempY;
        }*/
        //capture
        Set<int[]> captured = calCapture(differX, differY, emptyX, emptyY);
        if(captured.size() != 0){
            captured.add(empty);
            captureNum.add(captured);
            //System.out.println("(" + x + ", " + y + ") " + color);
            //System.out.println(captureNum.size());
        }
        //retreat
        Set<int[]> retreated = calRetreat(differX, differY);
        if(retreated.size() != 0){
            retreated.add(empty);
            retreatNum.add(retreated);
        }
        //without capture or retreat
        empties = checkEmpties();
    }

    private Set<int[]> calCapture(int differX, int differY, int emptyX, int emptyY){
        //System.out.println("    C differY, differX: " + differY + ", " + differX);
        Set<int[]> captured = new HashSet<>();
        int counter = 0;
        while(true){
            int newPieceX = emptyX + (differX * (counter + 1));
            int newPieceY = emptyY + (differY * (counter + 1));
            //System.out.println("        newPieceY, newPieceX: (" + newPieceY + ", " + newPieceX + ")");
            if(newPieceX < 0 || newPieceY < 0){
                //out of board: up left
                break;
            } else if(newPieceX >= boardData[0].length || newPieceY >= boardData.length){
                //out of board: down right
                break;
            }
            if(boardData[newPieceY][newPieceX] == opponent){
                //System.out.println("       newPieceY, newPieceX: (" + newPieceY + ", " + newPieceX + ")");
                captured.add(new int[]{newPieceY, newPieceX});
            } else {
                break;
            }
            counter++;
        }
        return captured;
    }

    private Set<int[]> calRetreat(int differX, int differY){
        //System.out.println("    R differY, differX: " + differY + ", " + differX);
        Set<int[]> retreated = new HashSet<>();
        int counter = 0;
        while(true){
            int newPieceX = x - (differX * (counter + 1));
            int newPieceY = y - (differY * (counter + 1));
            //System.out.println(newPieceX + " " +newPieceY);
            //System.out.println("        newPieceY, newPieceX: (" + newPieceY + ", " + newPieceX + ")");
            if(newPieceX < 0 || newPieceY < 0){
                //out of board: up left
                break;
            } else if(newPieceX >= boardData[0].length || newPieceY >= boardData.length){
                //out of board: down right
                break;
            }
            if(boardData[newPieceY][newPieceX] == opponent){
                //System.out.println("       newPieceY, newPieceX: (" + newPieceY + ", " + newPieceX + ")");
                //System.out.println("       boardData[newPieceY][newPieceX]: " + boardData[newPieceY][newPieceX]);
                retreated.add(new int[]{newPieceY, newPieceX});
            } else {
                break;
            }
            counter++;
        }
        return retreated;
    }

    public Set<int[]> checkEmpties(){
        Set<int[]> result = new HashSet<>();
        for(int i = y - 1; i < y + 2; i++){
            for(int j = x - 1; j < x + 2; j++){
                //System.out.println("(" + i + ", " + j + ")");
                if(i < 0 || j < 0 || i >= boardData.length - 1 || j >= boardData[0].length - 1){
                    continue;
                }
                //System.out.println(boardData[i][j]);
                if(boardData[i][j] == -1){
                    result.add(new int[]{i, j});
                }
            }
        }
        return result;
    }
    public void moveToNewPosition(int newY, int newX, int newColor){
        boardData[y][x] = -1;
        boardData[newY][newX] = newColor;
        pieces[y][x].color = -1;
        pieces[y][x].opponent = -1;
        pieces[newY][newX].color = newColor;
        if(newColor == 1){
            pieces[newY][newX].opponent = 0;
        } else {
            pieces[newY][newX].opponent = 1;
        }
    }
}
