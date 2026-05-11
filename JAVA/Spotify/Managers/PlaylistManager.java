package Spotify.Managers;

import java.util.ArrayList;
import Spotify.Models.Playlist;
import Spotify.Models.Song;

public class PlaylistManager {
    ArrayList<Playlist> playlists = new ArrayList<>();

    public String createPlaylist(String name) {
        Playlist playlist = this.findPlaylist(name);
        if (playlist == null) {
            playlists.add(new Playlist(name));
            return "Playlist created!";
        }
        return "Duplicate playlist exists!";
    }

    public String addSong(String playlistName, Song song) {
        Playlist playlist = this.findPlaylist(playlistName);
        if (playlist != null) {
            playlist.addSong(song);
            return "New song added!";
        }
        return "No such playlist found!";
    }

    public String deleteSong(String playlistName, Song song) {
        Playlist playlist = this.findPlaylist(playlistName);
        if (playlist != null) {
            playlist.removeSong(song);
            return "Song removed!";
        }
        return "No such playlist found!";
    }

    public String deletePlaylist(String playlistName) {
        Playlist playlist = this.findPlaylist(playlistName);
        if (playlist != null) {
            playlists.remove(playlist);
            return "Playlist deleted!";
        }
        return "No such playlist found!";
    }

    public String getSongs(String playlistName) {
        Playlist playlist = this.findPlaylist(playlistName);
        if (playlist != null) {
            StringBuilder sb = new StringBuilder();
            for (Song song : playlist.getSongs()) {
                sb.append(song.getName()).append(", ");
            }
            if (sb.length() > 0)
                sb.setLength(sb.length() - 2);
            return sb.toString();
        }
        return "No such playlist found!";
    }

    private Playlist findPlaylist(String playlistName) {
        for (Playlist playlist : playlists) {
            if (playlist.getName().equalsIgnoreCase(playlistName)) {
                return playlist;
            }
        }
        return null;
    }
}