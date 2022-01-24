import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.util.HashSet;
import java.util.Set;

public class Fanorona {
    public static void main(String[] args) {
        Fanorona fanoronaIns = new Fanorona();
/*
        int[] inputData = fanoronaIns.readInput();
        int rowSize = inputData[0];
        int columnSize = inputData[1];*/
        int rowSize = 5;
        int columnSize = 5;

        Board board = new Board(rowSize, columnSize);
        //board.print();
        System.out.println("When children's evaluation values are the same, the algorithm will randomly select one of them");
        System.out.println("---------Test 1---------");
        System.out.println("--------- 3*3 ---------");
        System.out.println("********* Brute-force, limit 20, 0 evaluation function *********");
        fanoronaIns.test(3, 3, 1, 20, 0);
        System.out.println("********* Brute-force, limit 20, 0 evaluation function *********");
        fanoronaIns.test(3, 3, 1, 20, 0);
        System.out.println("---------Test 2---------");
        System.out.println("--------- 5*5 ---------");
        System.out.println("********* Random *********");
        fanoronaIns.test(5, 5, 0, 0, 0);
        System.out.println("********* brute-force, limit 2, 1 evaluation function *********");
        fanoronaIns.test(5, 5, 1, 2, 1);
        System.out.println("--------- 5*9 ---------");
        System.out.println("********* Random *********");
        fanoronaIns.test(5, 9, 0, 0, 0);
        System.out.println("********* brute-force, limit 2, 1 evaluation function *********");
        fanoronaIns.test(5, 9, 1, 2, 1);
        System.out.println("---------Test 3---------");
        System.out.println("--------- 5*5 ---------");
        System.out.println("********* brute-force, limit 1, 0 evaluation function *********");
        fanoronaIns.test(5, 5, 1, 1, 0);
        System.out.println("********* brute-force, limit 2, 0 evaluation function *********");
        fanoronaIns.test(5, 5, 1, 2, 0);
        System.out.println("--------- 5*9 ---------");
        System.out.println("********* brute-force, limit 1, 0 evaluation function *********");
        fanoronaIns.test(5, 9, 1, 1, 0);
        System.out.println("********* brute-force, limit 2, 0 evaluation function *********");
        fanoronaIns.test(5, 9, 1, 2, 0);
        System.out.println("---------Test 4---------");
        System.out.println("--------- 5*5 ---------");
        System.out.println("********* brute-force, limit 2, 0 evaluation function *********");
        fanoronaIns.test(rowSize, columnSize, 1, 2, 0);
        System.out.println("********* brute-force, limit 2, 1 evaluation function *********");
        fanoronaIns.test(rowSize, columnSize, 1, 2, 1);
        System.out.println("--------- 5*9 ---------");
        System.out.println("********* brute-force, limit 2, 0 evaluation function *********");
        fanoronaIns.test(rowSize, columnSize, 1, 2, 0);
        System.out.println("********* brute-force, limit 2, 1 evaluation function *********");
        fanoronaIns.test(rowSize, columnSize, 1, 2, 1);
        System.out.println("---------Test 5---------");
        System.out.println("--------- 5*5 ---------");
        System.out.println("*********Alpha-beta, limit 2, 0 evaluation function *********");
        fanoronaIns.test(rowSize, columnSize, 2, 2, 0);
        System.out.println("*********Alpha-beta, limit 2, 1 evaluation function *********");
        fanoronaIns.test(rowSize, columnSize, 2, 2, 1);
        System.out.println("--------- 5*9 ---------");
        System.out.println("*********Alpha-beta, limit 2, 0 evaluation function *********");
        fanoronaIns.test(rowSize, columnSize, 2, 2, 0);
        System.out.println("*********Alpha-beta, limit 2, 1 evaluation function *********");
        fanoronaIns.test(rowSize, columnSize, 2, 2, 1);
        System.out.println("---------Test 6---------");
        System.out.println("--------- 5*5 ---------");
        System.out.println("********* brute-force, limit 2, 0 evaluation function *********");
        fanoronaIns.test(rowSize, columnSize, 1, 2, 0);
        System.out.println("*********Alpha-beta, limit 2, 0 evaluation function *********");
        fanoronaIns.test(rowSize, columnSize, 2, 2, 0);
        System.out.println("--------- 5*9 ---------");
        System.out.println("********* brute-force, limit 2, 0 evaluation function *********");
        fanoronaIns.test(rowSize, columnSize, 1, 2, 0);
        System.out.println("*********Alpha-beta, limit 2, 0 evaluation function *********");
        fanoronaIns.test(rowSize, columnSize, 2, 2, 0);
    }
    public void test(int row, int column, int solverNum, int depthLimit, int evaluationFunctionNum ){
        Board board = new Board(row, column);
        Player white = new Player(0, board);
        Player black = new Player(1, board);
        initialPlayers(board, white, black);
        int totalStates = 0;
        //System.out.println("----Game start----");
        Boolean gameFinish = false;
        int win = -1;
        int round = 0;
        while(!gameFinish){
            //System.out.print("Round: " + round);
            if(round % 2 == 0){
                //System.out.println(" White");
                white.solver(solverNum, depthLimit, evaluationFunctionNum);
            } else {
                //System.out.println(" Black");
                black.solver(solverNum, depthLimit, evaluationFunctionNum);
            }
            //board.print();
            white.setPieces();
            black.setPieces();
            board.resetPieceRemoveData();
            round++;
            win = board.checkWin();
            if(win >= 0){
                gameFinish = true;
            } else {
                gameFinish = false;
            }
            if(round == 1){
                //System.exit(0);
            }
        }
        //System.out.println("----Game finish----");

        if(win == 0){
            System.out.println("White win");
        } else if(win == 1) {
            System.out.println("Black win");
        } else {
            System.out.println("Not finishing yet");
        }
        System.out.println("Rounds: " + round);
        System.out.println("White total states: " + white.totalStates);
        System.out.println("Black total states: " + black.totalStates);
    }

    public int[] readInput(){
        DataInputStream in = new DataInputStream(new BufferedInputStream(System.in));
        String s;
        int[] data = new int[2];
        try {
            if((s = in.readLine()).length() != 0) {
                String[] split = s.split(" ");
                data[0] = Integer.parseInt(split[0]);
                data[1] = Integer.parseInt(split[1]);
            }

        } catch(Exception e) {
            e.printStackTrace();
            System.out.println("\nThe input number is invalid.\nPlease check your input.");
        }
        return data;
    }

    public void initialPlayers(Board board, Player white, Player black){
        Set<Piece> whitePieces = new HashSet<>();
        Set<Piece> blackPieces = new HashSet<>();
        for(int i = 0; i < board.row; i++){
            for(int j = 0; j < board.column; j++){
                if(board.pieces[i][j] != null){
                    if(board.pieces[i][j].color == 0){
                        whitePieces.add(board.pieces[i][j]);
                    } else if(board.pieces[i][j].color == 1){
                        blackPieces.add(board.pieces[i][j]);
                    }
                }
            }
        }
        white.setPieces();
        black.setPieces();
    }

}
