package modelosdatos;

public class Upload {
    //declara los valores que vamos a necesitar

    private String name;
    private String imgUrl;

    public Upload() {
    }
    //guardar datos
    public Upload(String name, String imgUrl) {
        this.name = name;
        this.imgUrl = imgUrl;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }
}
