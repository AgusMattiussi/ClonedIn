package ar.edu.itba.paw.webapp.dto;

import ar.edu.itba.paw.models.Category;

public class CategoryDTO {
    private String name;
    private long id;

    public static CategoryDTO fromCategory(final Category category) {
        final CategoryDTO dto = new CategoryDTO();
        dto.name = category.getName();
        dto.id = category.getId();

        return dto;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
}
