package Modelo.ClienteT;

public enum CondTributaria {

    Responsable_Inscripto("RI"),
    Monotributo("M"),
    Exento("E"),
    No_Responsable("NR"),
    Consumidor_Final("CF");
    
    private String condicion;
    
    private CondTributaria(String ct){
        this.condicion = ct;
    }

    public String getCondicion() {
        return condicion;
    }

    public void setCondicion(String condicion) {
        this.condicion = condicion;
    }
    
    
}
