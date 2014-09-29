TicTacToeClient is a Visual Studio Solution.
TicTacToeServer is an Eclipse project.

To open and modify the Visual Studio Solution it is necessary to install the XNA Game Studio specific for the current version of Visual Studio (https://msxna.codeplex.com/releases).

The .NET project requires some .class files from the TicTacToeServer project in order to run correctly. Those files are automatically compiled and copied in the asset folder using the Visual Studio Post-Build Event. Therefore the user only needs to launch the TicTacToeServer before the TicTacToeClient.