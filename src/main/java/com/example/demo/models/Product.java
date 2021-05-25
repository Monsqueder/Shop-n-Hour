package com.example.demo.models;

import javax.persistence.*;
import java.util.Collection;
import java.util.List;

@Entity
@Table
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private boolean isDisabled;

    private String name;

    private String company;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private Category category;

    private double price;

    private String description;

    private double rating;

    @ElementCollection
    private List<String> sizes;

    @ElementCollection
    private List<String> colors;

    @ElementCollection
    private List<String> img_name;

    private int order_count;

    @OneToMany(mappedBy = "product",
            orphanRemoval = true,
            fetch = FetchType.EAGER,
            cascade = CascadeType.ALL)
    private Collection<OrderLine> orderLines;

    @OneToMany(mappedBy = "product",
            orphanRemoval = true,
            fetch = FetchType.LAZY,
            cascade = CascadeType.ALL)
    private List<Comment> comments;

    public Product() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public boolean isDisabled() {
        return isDisabled;
    }

    public void setDisabled(boolean disabled) {
        isDisabled = disabled;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<String> getSizes() {
        return sizes;
    }

    public void setSizes(List<String> sizes) {
        this.sizes = sizes;
    }

    public List<String> getColors() {
        return colors;
    }

    public void setColors(List<String> colors) {
        this.colors = colors;
    }

    public List<String> getImg_name() {
        return img_name;
    }

    public void setImg_name(List<String> img_name) {
        this.img_name = img_name;
    }

    public int getOrder_count() {
        return order_count;
    }

    public void setOrder_count(int order_count) {
        this.order_count = order_count;
    }

    public Collection<OrderLine> getOrderLines() {
        return orderLines;
    }

    public void setOrderLines(Collection<OrderLine> orderLines) {
        this.orderLines = orderLines;
    }

    public List<Comment> getComments() {
        return comments;
    }

    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }
}
