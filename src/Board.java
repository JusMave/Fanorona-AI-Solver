import java.util.HashSet;
import java.util.Set;

public class Board {
    public Piece[][] pieces;
    public int[][] boardData;
    public int row;
    public int column;

    Board(int rowSize, int columnSize){
        int[][] boardTest = {
                {-1,-1,-1},
                { 0, 1, 0},
                {-1,-1, 0}};

        int[][] boardTest2 = {
                {1, 1, 1},
                {0, 0,-1},
                {0,-1, 0}};
        int[][] board33 = {
                {1, 1, 1},
                {0,-1, 1},
                {0, 0, 0}};
        int[][] board55 = {
                {1, 1, 1, 1, 1},
                {1, 1, 1, 1, 1},
                {1, 0,-1, 1, 0},
                {0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0}};
        int[][] board59 = {
                {1, 1, 1, 1, 1, 1, 1, 1, 1},
                {1, 1, 1, 1, 1, 1, 1, 1, 1},
                {1, 0, 1, 0,-1, 1, 0, 1, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 0}};

        row = rowSize;
        column = columnSize;
        if(row == 3 && column == 3){
            boardData = board33;
        } else if(row == 5 && column == 5){
            boardData = board55;
        } else if(row == 5 && column == 9) {
            boardData = board59;
        } else {
            System.out.println("Invalid row or column size");
            System.exit(1);
        }
        /*
        boardData = boardTest2;
        row = boardData.length;
        column = boardData[0].length;*/
        pieces = new Piece[row][column];
        for(int i = 0; i < row; i++){
            for(int j = 0; j < column; j++){
                if(boardData[i][j] >= -1){
                    Piece newPiece = new Piece(i, j, boardData[i][j], boardData, pieces);
                    pieces[i][j] = newPiece;
                }
            }
        }

    }

    Board(int[][] boardData){
        row = boardData.length;
        column = boardData[0].length;
        this.boardData = new int[row][column];
        pieces = new Piece[row][column];
        for(int i = 0; i < row; i++){
            for(int j = 0; j < column; j++){
                this.boardData[i][j] = boardData[i][j];
            }
        }
        for(int i = 0; i < row; i++){
            for(int j = 0; j < column; j++){
                if(this.boardData[i][j] >= -1){
                    Piece newPiece = new Piece(i, j, this.boardData[i][j], this.boardData, pieces);
                    pieces[i][j] = newPiece;
                }
            }
        }
    }

    public void print(){
        //System.out.println(row);
        //System.out.println(column);
        //System.out.println("Print board data: ");
        for(int i = 0; i < row; i++){
            for(int j = 0; j < column; j++){
                if(boardData[i][j] >= 0){
                    System.out.print(" " + boardData[i][j]);
                } else if(boardData[i][j] == -1){
                    System.out.print(boardData[i][j]);
                }
            }
            System.out.println();
        }
    }

    public void printPieces(){
        System.out.println("Print pieces: ");
        for(int i = 0; i < row; i++){
            for(int j = 0; j < column; j++){
                if(pieces[i][j].color == -1){
                    System.out.print("-1");
                } else if(pieces[i][j].color >= 0) {
                    System.out.print(" " + pieces[i][j].color);
                }
            }
            System.out.println();
        }
    }

    public Set<Piece> calPieceRemove(int player){
        Set<Piece> moveablePieces = new HashSet<>();
        Set<int[]> empties = findEmptyPoint();
        //System.out.println("empties.size " + empties.size());
        //traverse all the empties
        //System.out.println("player: " + player);
        //System.out.println("boardData.length: " + boardData.length + " boardData[0].length: " + boardData[0].length);
        for(int[] iA : empties){
            //System.out.println("empty y, x " + iA[0] + " " + iA[1]);
            //cal all the pieces around the empty point
            for(int i = iA[0] - 1; i < iA[0] + 2; i++){
                for(int j = iA[1] - 1; j < iA[1] + 2; j++){
                    if(i < 0 || j < 0 || i > boardData.length - 1 || j > boardData[0].length - 1){
                        //out of the board
                        continue;
                    } else {
                        //System.out.println("pieces[i][j].color: " + pieces[i][j].color + " " + player);
                        if(pieces[i][j].color == player){
                            //pieces belong to the player
                            pieces[i][j].checkMovable(iA);
                            moveablePieces.add(pieces[i][j]);
                        }
                    }
                }
            }
        }
        //System.out.println("************" + empties.size());
        return moveablePieces;
    }

    public Set<int[]> findEmptyPoint(){
        Set<int[]> empties = new HashSet<>();
        for(int i = 0; i < row; i++){
            for(int j = 0; j < column; j++){
                //System.out.println(i + " " + j);
                if(boardData[i][j] == -1){
                    empties.add(new int[]{i, j});
                }
            }
        }
        return empties;
    }

    public void setNewBoard(int[][] newBoardData){
        for(int i = 0; i < row; i++){
            for(int j = 0; j < column; j++){
                boardData[i][j] = newBoardData[i][j];
                pieces[i][j].color = newBoardData[i][j];
            }
        }
    }

    public void resetPieceRemoveData(){
        for(int i = 0; i < row; i++){
            for(int j = 0; j < column; j++){
                pieces[i][j].captureNum = new HashSet<>();
                pieces[i][j].retreatNum = new HashSet<>();
            }
        }
    }

    public int checkWin(){
        int whiteCounter = 0;
        int blackCounter = 0;
        for(int i = 0; i < row; i++){
            for(int j = 0; j < column; j++){
                if(boardData[i][j] == 0){
                    whiteCounter++;
                } else if(boardData[i][j] == 1){
                    blackCounter++;
                }
            }
        }
        if(whiteCounter == 0){
            return 1;
        } else if(blackCounter == 0){
            return 0;
        } else {
            return -1;
        }
    }
}
