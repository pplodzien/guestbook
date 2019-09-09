import java.time.LocalDate;
import java.time.LocalDateTime;

public class Note {
    private int noteId;
    private String message;
    private String author;
    private String date;


    public Note(int noteId, String message, String author, String date){
        this.noteId = noteId;
        this.message = message;
        this.author = author;
        this.date = date;
    }

    public int getNoteId() {
        return noteId;
    }

    public void setNoteId(int noteId) {
        this.noteId = noteId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}

