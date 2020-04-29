package com.example.collection.model;

import com.example.collection.database.DBConstants;

public class Item {
    int id;
    String category;
    String title;
    String type;
    String description;
    String author;

    public Item(int id, String category, String title, String description, String type, String author) {
        this(id, category, title, description, type);
        this.author = author;
    }

    public Item(int id, String category, String title, String description, String type) {
        this(category,title,description,type);
        this.id = id;

    }
    public Item(String category, String title, String description, String type) {
        this.category = category;
        this.type = type;
        this.title = title;
        this.description = description;
    }
    public Item(String category, String title, String description, String type,String author) {
        this.category = category;
        this.type = type;
        this.title = title;
        this.description = description;
        this.author = author;
    }

    public int getId() {
        return id;
    }


    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder( "" + category + "{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", type='" + type + '\'' +
                ", description='" + description + '\'' );
                if(category.equals(DBConstants.TABLE_NAME_BOOKS)) {
                    sb.append(", author='" + author + '\'');
                }
               sb.append( '}');
                return sb.toString();
    }
}
