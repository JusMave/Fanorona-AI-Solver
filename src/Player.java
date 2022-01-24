import java.util.HashSet;
import java.util.Random;
import java.util.Set;

public class Player {
    int color;
    Set<Piece> pieces;
    Board board;
    int solverNum = 1;
    int totalStates;
    Player(int playerColor, Board boardIns){
        color = playerColor;
        board = boardIns;
        totalStates = 0;
    }

    public void solver(int mode, int depthLimit, int evaluationFunctionNum){
        solverNum = mode;
        switch (solverNum){
            case 0:
                solverRandom();
                break;
            case 1:
                solverMinimax(depthLimit, evaluationFunctionNum);
                break;
            case 2:
                solverAlphaBeta(depthLimit, evaluationFunctionNum);
                break;
        }
    }

    private void solverRandom(){
        Set<Piece> movablePieces = findMovablePieces();
        Random r = new Random();
        int selectedPiece;
        if(movablePieces.size() == 1){
            selectedPiece = 0;
        } else {
            if(movablePieces.size() == 0){
                selectedPiece = 0;
            } else {
                selectedPiece = r.nextInt(movablePieces.size());
            }

        }
        int counter = 0;
        int selectedAction = 0;
        for(Piece p : movablePieces){
            if(counter == selectedPiece){
                //choose capture or retreat action
                Set<Set<int[]>> SelectedActionSet = new HashSet<>();
                int captureOrRetreat;if(p.captureNum.size() == 0 && p.retreatNum.size() != 0){
                    captureOrRetreat = 1;
                } else if(p.captureNum.size() != 0 && p.retreatNum.size() == 0){
                    captureOrRetreat = 0;
                } else if(p.captureNum.size() == 0 && p.retreatNum.size() == 0){
                    captureOrRetreat = -1;
                } else {
                    captureOrRetreat = r.nextInt(2);
                }
                if(captureOrRetreat == 0){
                    //choose capture
                    //System.out.println("capture:");
                    selectedAction = r.nextInt(p.captureNum.size());
                    SelectedActionSet = p.captureNum;
                } else if (captureOrRetreat == 1) {
                    //choose retreat
                    //System.out.println("retreat:");
                    selectedAction = r.nextInt(p.retreatNum.size());
                    SelectedActionSet = p.retreatNum;
                } else if(captureOrRetreat == -1){
                    //choose empty
                    //System.out.println("empty:");
                    SelectedActionSet.add(p.checkEmpties());
                }
                //select action
                int actionCounter = 0;

                for(Set<int[]> s : SelectedActionSet){
                    if(actionCounter == selectedAction){
                        int[] empty = new int[2];
                        int[][] empties = new int[s.size()][2];
                        int counterEmpty = 0;
                        for(int[] i : s){
                            if(board.boardData[i[0]][i[1]] == -1){
                                empties[counterEmpty][0] = i[0];
                                empties[counterEmpty][1] = i[1];
                            }
                        }
                        Set<int[]> adjacentEmpties = p.checkEmpties();
                        int findEmpty = 0;
                        for(int i = 0; i < empties.length; i++){
                            empty[0] = empties[i][0];
                            empty[1] = empties[i][1];
                            for(int[] iA : adjacentEmpties){
                                /*
                                System.out.println("empty" + empty[0] + " " + empty[1]);
                                System.out.println("ia" + iA[0] + " " + iA[1]);*/
                                if(iA[0] == empty[0] && iA[1] == empty[1]){
                                    //System.out.println("++++++");
                                    findEmpty = 1;
                                    break;
                                }
                            }
                            if(findEmpty == 1){
                                break;
                            }
                        }
                        //remove all the opponent's pieces
                        for(int[] i : s){
                            board.boardData[i[0]][i[1]] = -1;
                            board.pieces[i[0]][i[1]].color = -1;
                        }
                        //move current piece to the empty point
                        p.moveToNewPosition(empty[0], empty[1], p.color);

                    }
                    actionCounter++;
                }
            }
            counter++;
        }
    }

    private void solverMinimax(int depthLimit, int evaluationFunctionNum){
        MinimaxTree tree = new MinimaxTree(board.boardData, color, depthLimit + 1, evaluationFunctionNum);
        tree.buildTree(tree.root);
        tree.setEvaluationValue(tree.root);
        int[][] newBoardData = tree.getNextBoardData(tree.root);
        board.setNewBoard(newBoardData);
        tree.calTotalStates(tree.root);
        totalStates += tree.totalStates;
        //System.out.println("Total states: " + tree.totalStates);
    }

    private void solverAlphaBeta(int depthLimit, int evaluationFunctionNum){
        MinimaxTree tree = new MinimaxTree(board.boardData, color, depthLimit + 1, evaluationFunctionNum);
        tree.buildTree(tree.root);
        tree.setEvaluationValueAlphaBeta(tree.root);
        int[][] newBoardData = tree.getPathAlphaBeta(tree.root);
        board.setNewBoard(newBoardData);
        totalStates += tree.totalStates;
        //System.out.println("Total states: " + tree.totalStates);
    }

    private void doAction(Piece p, Set<int[]> s){
        int[] empty = new int[2];
        int[][] empties = new int[s.size()][2];
        int counterEmpty = 0;
        for(int[] i : s){
            if(board.boardData[i[0]][i[1]] == -1){
                empties[counterEmpty][0] = i[0];
                empties[counterEmpty][1] = i[1];
            }
        }
        Set<int[]> adjacentEmpties = p.checkEmpties();
        int findEmpty = 0;
        for(int i = 0; i < empties.length; i++){
            empty[0] = empties[i][0];
            empty[1] = empties[i][1];
            for(int[] iA : adjacentEmpties){
                                /*
                                System.out.println("empty" + empty[0] + " " + empty[1]);
                                System.out.println("ia" + iA[0] + " " + iA[1]);*/
                if(iA[0] == empty[0] && iA[1] == empty[1]){
                    //System.out.println("++++++");
                    findEmpty = 1;
                    break;
                }
            }
            if(findEmpty == 1){
                break;
            }
        }
        //remove all the opponent's pieces
        for(int[] i : s){
            board.boardData[i[0]][i[1]] = -1;
            board.pieces[i[0]][i[1]].color = -1;
        }
        //move current piece to the empty point
        p.moveToNewPosition(empty[0], empty[1], p.color);
    }

    private Set<Piece> findMovablePieces(){
        Set<Piece> movablePieces = new HashSet<>();
        board.calPieceRemove(color);
        for(Piece p : pieces){
            //System.out.println("(" + p.x + ", " + p.y + ") " + p.color);
            //System.out.println("    capture: " + p.captureNum.size());
            for(Set<int[]> iS : p.captureNum){
                if(iS.size() != 0){
                    movablePieces.add(p);
                }
            }
            for(Set<int[]> iS : p.retreatNum){
                if(iS.size() != 0){
                    movablePieces.add(p);
                }
            }
        }
        //if can't remove any opponent's pieces
        if(movablePieces.size() == 0){
            //System.out.println("can't remove any opponent's pieces");
            for(Piece p : pieces){
                //System.out.println("p xy " + p.x + " " + p.y);
                //System.out.println("p.checkEmpties().size(): " + p.checkEmpties().size());
                if(p.checkEmpties().size() != 0){
                    movablePieces.add(p);
                }
            }
        }
//        for(Piece p : movablePieces){
//            System.out.println(p.x + " " + p.y);
//        }
        return movablePieces;

    }

    public void setPieces(){
        pieces = new HashSet<>();
        for(int i = 0; i < board.row; i++){
            for(int j = 0; j < board.column; j++){
               if(board.boardData[i][j] == color){
                   pieces.add(board.pieces[i][j]);
               }
            }
        }
    }

    public void printPieces(){
        int counter = 0;
        for(Piece p : pieces){
            System.out.print("(" + p.x + ", " + p.y + ") ");
            if(counter == 5){
                System.out.println();
            }
            counter++;
        }
        System.out.println();
    }
}
