Optional Features implemented:
    6. The user may trigger a scan in a cell with a mine. This is done by tapping the cell showing a revealed mine. This scan is identical to scans in other cells
        -I added this

    1.3 The theme may affect the name given to your application; it need not be Mine Seeker
        -I gave the game a water theme where you are trying to destroy submarines, hence the name: "Submarine Wars"


    1.3 Welcome screen may include two or more different animations (such as fade, spin, or move). It may have complicated animations such as rotating and moving a block of elements at once.
        -I added multiple animations in the Welcome screen, including a pulsing title, and multiple images, buttons, and text sliding in, and a submarine swimming around the screen
        -The pulsing title uses a scaling animation, the others use translational animations

    1.5 The Welcome screen may automatically advance to the Main Menu after all animations (if any) have finished, plus at least 4 extra seconds
        -I added this (4 seconds after visible animations stop)

    2.5 Buttons displayed may be fancy and visually appealing featuring icons.
        -I didn't want to put icons in the buttons due to not matching my theme, but they are made visually appealing

    3.13 May display text stating the total number of games started (saved between application launches). 
        -This is shown in the bottom right after pressing Play (starting a game)

    3.14 May display text stating the best score so far of any completed game of this specific configuration (board size and number of mines); must save best score for each possible configuration
        -After playing a game, the Congratulations popup includes the high score for the given configuration. In addition, you can click on "Options", and then "See Top Scores" to view your top score at any time
    5.4 May allow user to reset number of times game has been played and best scores for each game configuration (if supported).
        -Go to the Options screen to do this "Reset Top Scores/Games Played". It will ask for your confirmation

    5.5 App may save game state if app is closed while playing
        -Closing the app saved the game by default, and it will resume the next time you open the app and play a game
        -Also, if you click the yellow back arrow while you're in a game, it asks if you want to save or not save. If you click save, the next time you enter a game, it will be your saved game. Clicking not save will discard the current game.


    Also:
        -When the game is complete, a pop-up window slides in from the left showing you the Congratulations/Mission Successful message while also showing the playing grid in the background
        -Added certain animations:
            -Moving back arrow, pulsing title, buttons slide in one at a time when opening Main Menu activity
        -Help screen is scrollable to view developer info and citations
        -Added a button to view all current high scores in the Options Screen
        -Added a pause game option to the Game activity that is triggered when you click the back button
       
