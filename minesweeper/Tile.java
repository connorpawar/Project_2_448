package minesweeper;

import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.ImageIcon;
//AWT imports
import java.awt.Insets;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseAdapter;
import java.awt.event.InputEvent;
//Non-GUI related imports
import java.io.IOException;

/**
 * An extension of the JButton class to be used as tiles of a Minesweeper game.
 * */
class Tile extends JButton {
    /* Constants for Tiles */
	/** The length and width of all tiles */
    private static final int mTileSize = 30;

    /* Member variables of tiles */
    /** holds the number of adjacent mines*/
    private int mSurroundingMines;
    /** true if the tile is currently flagged */
    private boolean mFlagged;
    /** true if the tile is a mine */
    private boolean mIsMine;
    /** true if the tile is open */
    private boolean mOpened;
    /** the x coordinate in the board */
    private int x;
    /** the y coordinate in the board */
    private int y;

    /* The ImageIcons used to display the icons */
    /** Image for flag used on tiles*/
    private static ImageIcon mFlaggedIcon;
    /** Image for mine used on tiles*/
    private static ImageIcon mMineIcon;
    /** Image for tile used on tiles */
    private static ImageIcon mTileIcon;
    /** Image for a pressed tile if surrounding mines are 0 use on tile */
    private static ImageIcon mPressedIcon;

    /** animation icons*/
    private static ImageIcon mExplIcon1;
    private static ImageIcon mExplIcon2;
    private static ImageIcon mExplIcon3;
    private static ImageIcon mExplIcon4;
    private static ImageIcon mExplIcon5;
    private static ImageIcon mExplIcon6;
    
    /**
     * MouseListener that controls the tiles right and left click
     */
    private MouseListener mouseListener = new MouseAdapter() {
        public void mousePressed(MouseEvent mouseEvent) {
            int modifiers = mouseEvent.getModifiers();
            Sound makeNoise = new Sound();
            //left button clicked
            if ((modifiers & InputEvent.BUTTON1_MASK) == InputEvent.BUTTON1_MASK) {
                if (!mFlagged) {
                    if (mIsMine) {
                        setMineIcon();
                        makeNoise.playBomb();
                        Game_Driver.gameOver();
                    } else {
                        if (mSurroundingMines > 0) {

                            displaySurroundingMines();
                        } else {
                        	//displaySurroundingMines();
                            setNullIcon();
                            Game_Driver.openTile(x, y);
                        }
                        if (Game_Driver.isEndPossible()) {
                            Game_Driver.gameWin();
                        }
                        Game_Driver.updateMineNums();
                    }
                }
            }
            //right button clicked
            if ((modifiers & InputEvent.BUTTON3_MASK) == InputEvent.BUTTON3_MASK) {
                setFlagged(mFlagged);
            }
        }
    };

    /////////////////////////////////////////////////////////
    //Constructor
    /////////////////////////////////////////////////////////

    /**
     * Constructs default tile sets default tile icon, size, visibility, margin, and mouse listener
     */
    Tile() {
        super();

        mSurroundingMines = 0;
        mFlagged = false;
        mIsMine = false;
        setText("");
        setMargin(new Insets(0, 0, 0, 0));
        setPreferredSize(new Dimension(mTileSize, mTileSize));
        setIcon(mTileIcon);
        setSize(mTileSize, mTileSize);
        setVisible(true);

        addMouseListener(mouseListener);
    }
    /**
     * Constructs copy of input tile
     */
    Tile(Tile copy) {
        super();

        mSurroundingMines = copy.mSurroundingMines;
        mFlagged = false;
        mIsMine = copy.mIsMine;
        setText("");
        setMargin(new Insets(0, 0, 0, 0));
        setPreferredSize(new Dimension(mTileSize, mTileSize));
        setIcon(mTileIcon);
        setSize(mTileSize, mTileSize);
        setVisible(true);

        addMouseListener(mouseListener);
    }

    /////////////////////////////////////////////////////////
    //Getters
    /////////////////////////////////////////////////////////

    /**
     * returns mFlagged
     *
     * @return True if tile is flagged
     */
    boolean getFlagged() {
        return mFlagged;
    }

    /**
     * returns mSurroundingMines
     *
     * @return The number of adjacent mines
     */
    Integer getSurroundingMines() {
        return mSurroundingMines;
    }

    /**
     * returns mIsMine
     *
     * @return True if the tile is a mine
     */
    boolean getIsMine() {
        return mIsMine;
    }

    /**
     * Says if the tile can be opened
     *
     * @return True if tile can be opened, not open and not flagged
     */
    boolean canOpen() {
        return (!mOpened && !mFlagged);
    }

    /////////////////////////////////////////////////////////
    //Setters
    /////////////////////////////////////////////////////////

    /**
     * sets a tile icon to null
     */
    private void setNullIcon() {
        setIcon(null);
        setDisable();
    }

    /**
     * Sets a the mine icon on the tile
     */
    void setMineIcon() {
        setIcon(mMineIcon);
        setDisable();
    }
    
    /**
     * Sets explosion icons on the tile
     */
    void setExpl1() {
        setIcon(mExplIcon1);
    }
    void setExpl2() {
        setIcon(mExplIcon2);
    }
    void setExpl3() {
        setIcon(mExplIcon3);
    }
    void setExpl4() {
        setIcon(mExplIcon4);
    }
    void setExpl5() {
        setIcon(mExplIcon5);
    }
    void setExpl6() {
        setIcon(mExplIcon6);
    }

    /**
     * Makes the tile unclickable by disabling it and removing mouse listener
     */
    void setDisable() {
    	mOpened = true;
        setEnabled(false);
        removeMouseListener(mouseListener);
    }

    /**
     * Set the tile icon to a flag image and sets member variable mFlagged
     *
     * @param flagged True if tile show be flagged
     */
    private void setFlagged(boolean flagged) {
        if (!flagged && Board.getFlagCount() != 0 && !mOpened) {
            setIcon(mFlaggedIcon);
            mFlagged = true;
            Board.decrementFlagCount();
            if (Board.getFlagCount() == 0) {
                if (Game_Driver.isEndPossible()) {
                    Game_Driver.gameWin();
                }
            }
        } else if (mFlagged) {
            setIcon(mTileIcon);
            mFlagged = false;
            Board.incrementFlagCount();
        }
    }

    /**
     * Generating the ImageIcons using ImageIO.read
     */
    void setIcons() {
        try {
            Image img = null;
            try {
                img = ImageIO.read(getClass().getResource("Resources/flag.png"));
            } catch (IOException e1) {
                e1.printStackTrace();
            }
            mFlaggedIcon = new ImageIcon(img);

            try {
                img = ImageIO.read(getClass().getResource("Resources/MineIcon.png"));
            } catch (IOException e1) {
                e1.printStackTrace();
            }
            mMineIcon = new ImageIcon(img);

            try {
                img = ImageIO.read(getClass().getResource("Resources/TileIcon.png"));
            } catch (IOException e1) {
                e1.printStackTrace();
            }
            mTileIcon = new ImageIcon(img);

            try {
                img = ImageIO.read(getClass().getResource("Resources/PressedIcon.png"));
            } catch (IOException e1) {
                e1.printStackTrace();
            }
            mPressedIcon = new ImageIcon(img);

            try {
                img = ImageIO.read(getClass().getResource("Resources/expl1.jpg"));
            } catch (IOException e1) {
                e1.printStackTrace();
            }
            mExplIcon1 = new ImageIcon(img);
            
            try {
                img = ImageIO.read(getClass().getResource("Resources/expl2.jpg"));
            } catch (IOException e1) {
                e1.printStackTrace();
            }
            mExplIcon2 = new ImageIcon(img);
            
            try {
                img = ImageIO.read(getClass().getResource("Resources/expl3.jpg"));
            } catch (IOException e1) {
                e1.printStackTrace();
            }
            mExplIcon3 = new ImageIcon(img);
            
            try {
                img = ImageIO.read(getClass().getResource("Resources/expl4.jpg"));
            } catch (IOException e1) {
                e1.printStackTrace();
            }
            mExplIcon4 = new ImageIcon(img);

            try {
                img = ImageIO.read(getClass().getResource("Resources/expl5.jpg"));
            } catch (IOException e1) {
                e1.printStackTrace();
            }
            mExplIcon5 = new ImageIcon(img);
            
            try {
                img = ImageIO.read(getClass().getResource("Resources/expl6.jpg"));
            } catch (IOException e1) {
                e1.printStackTrace();
            }
            mExplIcon6 = new ImageIcon(img);
        } catch (Exception e) {
            System.out.println("ImageIcons not set successfully.");
        }
    }

    /**
     * Sets the x value of a tile for use in openTile(), called upon tile creation in Board.java
     *
     * @param i The new x coordinate
     */
    void setX(int i) {
        x = i;
    }

    /**
     * Sets the y value of a tile for use in openTile(), called upon tile creation in Board.java
     *
     * @param j The new y coordinate
     */
    void setY(int j) {
        y = j;
    }

    /**
     * Sets mSurroundingMines
     *
     * @param mineCount The number of adjacent mines
     */
    void setSurroundingMines(int mineCount) {
        mSurroundingMines = mineCount;
    }

    /**
     * Sets the flag if the tile is a mine
     *
     * @param isMine The new mIsMine value
     */
    void setIsMine(boolean isMine) {
        mIsMine = isMine;
    }

    /////////////////////////////////////////////////////////
    //METHODS
    /////////////////////////////////////////////////////////

    /**
     * Displays the number of adjacent mines, disables tile after
     * Does not display 0 for number of mines, is left with pressed tile icon
     */
    void displaySurroundingMines() {

        if (mSurroundingMines == 0) {
        	setText("");
            setIcon(mPressedIcon);
        } else if (mSurroundingMines > 0) {
            try {
                setIcon(null);
                setText(Integer.toString(mSurroundingMines));
            } catch (Exception e) {
                System.out.println("Error in displaying numbered tile.");
            }
        }
        setDisable();
    }

    /**
     * Sets mOpened to true
     */
    void setIsOpened() {
        mOpened = true;
    }

    /**
     * Returns mOpened
     */
    public boolean getIsOpened() {
        return mOpened;
    }

    /**
     * changes what the tile should look like on the cheat mode board
     *
     * @ms.Pre-condition cheat mode is activated
     * @ms.Post-condition The tile on the cheat mode board displays the image
     * corresponding to the real board
     *
     * @see Game_Driver#CheatUpdate()
     * @see Game_Driver#CheatMode()
     * */
	  void TileCheat() {
		    setIsOpened();
		    if(mIsMine) {
			       setMineIcon();
    		}
    		else if(mSurroundingMines==0) {
             setNullIcon();
    		}
    		else {
    			   displaySurroundingMines();
    		}
	  }

    /**
     * sets mIsMIne to false;
     */
    void removeMine() {
    	mIsMine = false;
    }
}
