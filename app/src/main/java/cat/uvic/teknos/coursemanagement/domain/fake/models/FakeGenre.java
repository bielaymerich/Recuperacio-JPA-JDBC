package cat.uvic.teknos.coursemanagement.domain.fake.models;

public class FakeGenre implements cat.uvic.teknos.coursemanagement.models.Genre {
    private int id;
    private String description;

    @Override
    public int getId() {
        return id;
    }

    @Override
    public void setId(int id) {
        this.id = id;
    }

    @Override
    public String getDescription() {
        return description;
    }

    @Override
    public void setDescription(String description) {
        this.description = description;
    }
}
