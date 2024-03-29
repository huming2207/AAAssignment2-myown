package mazeGenerator;

import maze.Cell;
import maze.Maze;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Collections;

/**
 * Modified Prim's Generator
 *
 * @author Ming Hu s3554025
 */
public class ModifiedPrimsGenerator implements MazeGenerator
{

    /**
     * The set Z in assignment spec
     */
    private HashSet<Cell> setZ;

    /**
     * The set F in assignment spec
     */
    private HashSet<Cell> setF;

    public ModifiedPrimsGenerator()
    {
        setZ = new HashSet<>();
        setF = new HashSet<>();
    }

    @Override
    public void generateMaze(Maze maze)
    {
        initPrim(maze);

    } // end of generateMaze()


    /**
     * Includes:
     *  Step 1.1: Pick a random starting cell
     *
     * @param maze
     */
    private void initPrim(Maze maze)
    {
        // Use the original isPerfect() method to detect if this maze is perfect
        // The loop may happens with ~10% possibility
        //
        // Based on Section 3, there is no tunnel maze in Prim's generator
        runPrim(maze.entrance, maze.sizeR * maze.sizeC);

        while(!maze.isPerfect())
        {
            // Forget about the size of the tunnel, it doesn't actually take any effects lol.
            maze.initMaze(maze.sizeR, maze.sizeC,
                    maze.entrance.r, maze.entrance.c,
                    maze.exit.r, maze.exit.c,
                    new ArrayList<>(maze.sizeTunnel));

            initPrim(maze);
        }
    }

    /**
     *
     * Recursively do the Prim's algorithm
     *
     * Includes:
     *  Step 1.2: add the random cell to set Z;
     *  Step 1.3: Put all neighbouring cells of starting cell into the frontier set F;
     *  Step 2.1: Randomly select a cell c from the frontier set and remove it from F;
     *  Step 2.2: Randomly select a cell b that is in Z and adjacent to the cell c.
     *  Step 2.3: Carve a path between c and b.
     *  Step 3: (Repeat 1.2/1.3)
     *  Step 4: Repeat until Z includes every cell in the maze.
     *
     * @param rootCell The root cell (Cell "c" in the assignment spec)
     * @param sizeOfMaze Size of the maze (row * column)
     */
    private void runPrim(Cell rootCell, int sizeOfMaze)
    {
        // Step 1.2 Add the root cell to original cell list
        setZ.add(rootCell);

        // Step 1.3 Add all neighbor cell of the root cell to the neighbor cell list
        Collections.addAll(setF, rootCell.neigh);

        // Step 2.1 Randomly select a cell "c" from the frontier set "F"...
        Cell cellC = GeneratorHelper.pickRandomCellFromSet(setF);

        // Step 2.1 ...and remove from it
        setF.remove(cellC);

        // Step 2.2 Randomly select a cell "b" that is in Z and adjacent to the cell "c"
        Cell cellB = GeneratorHelper.pickRandomCellFromSet(setZ);
        while(!GeneratorHelper.isNeighbor(cellB, cellC))
        {
            cellB = GeneratorHelper.pickRandomCellFromSet(setZ);
        }

        // Step 2.3 Carve a path between Cell "b" and "c"
        if(!setZ.contains(cellC))
        {
            GeneratorHelper.rebuildWall(cellB, cellC);
        }
        else
        {
            return;
        }

        // Step 3/4
        while(setZ.size() < sizeOfMaze)
        {
            runPrim(cellC, sizeOfMaze);
        }
    }


} // end of class ModifiedPrimsGenerator
