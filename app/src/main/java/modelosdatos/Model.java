package modelosdatos;

public class Model {

    //declara los valores que vamos a necesitar
    private String id;
    private String group;
    private String materia;
    private String activity;

    public Model() {
        //construir un objeto sin datos
    }


    //consultas
    public Model(String id, String group, String materia, String activity) {
        this.id = id;
        this.group = group;
        this.materia = materia;
        this.activity = activity;
    }

    //guardar datos
    public Model(String group, String materia, String activity) {
        this.group = group;
        this.materia = materia;
        this.activity = activity;
    }



    //Id

    public String getId() {
        return id;
    }
    //no retorna nada
    public void setId(String id) {
        this.id = id;
    }


    //Group
    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }


    //Materias
    public String getMateria() {
        return materia;
    }

    public void setMateria(String materia) {
        this.materia = materia;
    }


    //Activity
    public String getActivity() {
        return activity;
    }

    public void setActivity(String activity) {
        this.activity = activity;
    }
}
