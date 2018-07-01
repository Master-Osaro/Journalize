package com.inc.os_i.journalize.model;

public class JournalEntry {

    private String journalContent, title, date, key;


    public JournalEntry() {}

    public JournalEntry(String title, String journalContent, String date) {
        this.journalContent = journalContent;
        this.date = date;
        this.title = title;

    }

    public JournalEntry(String title, String journalContent, String date, String key) {
        this.journalContent = journalContent;
        this.date = date;
        this.title = title;
        this.key = key;

    }
    public JournalEntry(String journalContent) {
        this.journalContent = journalContent;
    }

    public String getJournalContent() {
        return journalContent;
    }

    public void setJournalContent(String journalContent) {
        this.journalContent = journalContent;
    }

    public String getDate(){
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
}
