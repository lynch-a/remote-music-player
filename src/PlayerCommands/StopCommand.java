package PlayerCommands;

import Player.Mp3;

public class StopCommand implements PlayerCommand {
		private Mp3 fileToPlay;
		
		public StopCommand(Mp3 fileToPlay) {
			this.fileToPlay = fileToPlay;
		}
		
		public Mp3 getFileToPlay() {
			return fileToPlay;
		}
}
