package vista;

import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import controlador.InstitutoMeteorologia;
import controlador.MeteorologiaException;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.plaf.FontUIResource;
import javax.swing.text.StyleContext;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Locale;

public class VentanaPrincipal extends JFrame {
    private InstitutoMeteorologia instituto;
    private InterfazUsuario interfaz;
    private JPanel panelPrincipal;
    private JButton salirButton;
    private JPanel panelSalir;
    private JButton crearRegionButton;
    private JButton cargarDatosButton;
    private JButton crearComunaButton;
    private JButton importarDatosDeTextoButton;
    private JButton guardarDatosButton;
    private JButton crearEstacionMeteorologicaButton;
    private JButton instalarSensorButton;
    private JButton registrarMedicionButton;
    private JButton cambiarEstadoDeSensorButton;
    private JPanel panelListados;
    private JComboBox cmbListados;
    private JButton generarListadoButton;
    private JPanel panelRegistro;
    private JPanel panelDatos;
    private JPanel panelIzqInferior;
    private JLabel lblEstado;
    private JPanel panelTitulo;

    private static final String[] LISTADOS = {
            "Alertas de una estación",
            "Regiones",
            "Comunas",
            "Estaciones de una comuna",
            "Sensores de una estación",
            "Mediciones de un sensor",
            "Resumen estadístico de un sensor"
    };

    public VentanaPrincipal(){
        instituto = InstitutoMeteorologia.getInstance();
        interfaz = InterfazUsuario.getInstance();

        setTitle("Sistema de Información Meteorológica");
        setContentPane(panelPrincipal);
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);

        actualizarEstado();
        pack();
        setLocationRelativeTo(null);

        addWindowListener(new WindowAdapter() { //este addWindowListener es para que se pueda cerrar con x y en vez de que se cierre invoque el metodo de confirmarSalida.            @Override
            public void windowClosing(WindowEvent e) {
                confirmarSalida();
            }
        });
        salirButton.addActionListener(e -> confirmarSalida());
        crearRegionButton.addActionListener(e -> crearRegion());
        crearComunaButton.addActionListener(e -> crearComuna());
        crearEstacionMeteorologicaButton.addActionListener(e -> crearEstacionMeteorologica());
        instalarSensorButton.addActionListener(e-> abrirVentanaInstalarSensor());
        registrarMedicionButton.addActionListener(e-> abrirVentanaRegistrarMedicion());
        cambiarEstadoDeSensorButton.addActionListener(e-> abrirVentanaCambiarEstadoSensor());
        cargarDatosButton.addActionListener(e -> gestionarDatos("CARGAR"));
        importarDatosDeTextoButton.addActionListener(e -> gestionarDatos("IMPORTAR"));
        guardarDatosButton.addActionListener(e -> gestionarDatos("GUARDAR"));
        generarListadoButton.addActionListener(e-> seleccionarListado());
    }

    private void actualizarEstado(){
        if(instituto.existenDatos() == true){
            lblEstado.setText("Estado: el sistema tiene datos registrados.");
        }else{
            lblEstado.setText("Estado: el sistema no tiene datos registrados.");
        }
    }

    private void confirmarSalida() {
        InstitutoMeteorologia instituto = InstitutoMeteorologia.getInstance();
        if (!instituto.existenDatos()) {
            dispose();
            return;
        }
        int respuesta = JOptionPane.showConfirmDialog(this, "Desea guardar antes de salir", "Confirmar salida", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE);
        switch (respuesta) {
            case JOptionPane.YES_OPTION -> {
                if (gestionarDatos("GUARDAR")) {
                    dispose();
                }
            }
            case JOptionPane.NO_OPTION -> {
                dispose();
            }
            case JOptionPane.CANCEL_OPTION -> {
            }
            default -> {
            }
        }
    }

    private void crearRegion() {
        setVisible(false);
        try{
            interfaz.crearRegion();
        }finally{
            setVisible(true);
            actualizarEstado();
        }
    }

    private void crearComuna() {
        setVisible(false);
        try{
            interfaz.crearComuna();
        }finally {
            setVisible(true);
            actualizarEstado();
        }
    }

    private void crearEstacionMeteorologica() {
        setVisible(false);
        try{
            interfaz.crearEstacionMeteorologica();
        }finally {
            setVisible(true);
            actualizarEstado();
        }
    }
    private void abrirVentanaInstalarSensor(){
        if(instituto.getCodigosEstaciones().length == 0){
            JOptionPane.showMessageDialog(this, "No existen estaciones registradas.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        new VentanaInstalarSensor(this).setVisible(true);
        actualizarEstado();
    }

    private void abrirVentanaRegistrarMedicion(){
        if(instituto.getCodigosEstaciones().length == 0){
            JOptionPane.showMessageDialog(this, "No existen estaciones registradas.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        new VentanaRegistrarMedicion(this).setVisible(true);
        actualizarEstado();
    }

    private void abrirVentanaCambiarEstadoSensor(){
        if(instituto.getCodigosEstaciones().length == 0){
            JOptionPane.showMessageDialog(this, "No existen estaciones registradas.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        new VentanaCambiarEstadoSensor(this).setVisible(true);
        actualizarEstado();
    }

    private boolean gestionarDatos(String tipoOperacion) {//hice un switch para simplificar todo lo de los datos en una funcion booleana para manejar excepcionaes
        String titulo;
        String mensaje;
        try {
            switch (tipoOperacion) {
                case "CARGAR" -> {
                    instituto.cargaDatos();
                    titulo = "Cargar datos";
                    mensaje = "Datos cargados correctamente en el archivo datos/meteorologia.dat";
                }

                case "GUARDAR" -> {
                    instituto.guardaDatos();
                    titulo = "Guardar datos";
                    mensaje = "Datos guardados correctamente en el archivo datos/meteorologia.dat";
                }

                case "IMPORTAR" -> {
                    instituto.importaDatosTexto();
                    titulo = "Importar datos de texto";
                    mensaje = "Datos de texto importados correctamente ";
                }

                default -> {
                    return false;
                }
            }
            actualizarEstado();
            JOptionPane.showMessageDialog(this, mensaje, titulo, JOptionPane.INFORMATION_MESSAGE);
            return true;
        } catch (MeteorologiaException e) {
            actualizarEstado();
            JOptionPane.showMessageDialog(this, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }
    private void seleccionarListado(){
        String opcion = (String) cmbListados.getSelectedItem();
        if(opcion == null || cmbListados.getSelectedIndex() == 0){
            JOptionPane.showMessageDialog(this,"Debes seleccionar una opción","Advertencia",JOptionPane.WARNING_MESSAGE);
            return;
        }

        if("Alertas de una estación".equals(opcion)){
            if(instituto.getCodigosEstaciones().length == 0){
                JOptionPane.showMessageDialog(this, "No existen estaciones registradas.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            new VentanaListadoAlertas(this).setVisible(true);
            return;
        }

        setVisible(false);

        try{
            switch (opcion){
                case "Regiones"-> interfaz.listarRegiones();
                case "Comunas"-> interfaz.listarComunas();
                case "Estaciones de una comuna" -> interfaz.listarEstaciones();
                case "Sensores de una estación" -> interfaz.listarSensores();
                case "Mediciones de un sensor" -> interfaz.listarMediciones();
                case "Resumen estadístico de un sensor" -> interfaz.listarResumenMediciones();
                default ->{

                }
            }
        }finally{
            setVisible(true);
            actualizarEstado();
        }
    }

    //metodos auxiliares
    private Float solicitarFloatValido(String mensaje) {//Metodo auxiliar para crearEstacion Meteorologica ya que se solicitan float con los JOpane que leen String.
        while (true) {
            String texto = JOptionPane.showInputDialog(this, mensaje);
            if (texto == null) {
                return null; //El usuario puso cancelar, por ende salimos con null
            }
            try {
                float numeroValido = Float.parseFloat(texto.trim().replace(',', '.'));//el replace no me lo sabia asi que le pregunte a chatgpt
                if (!Float.isFinite(numeroValido)) {
                    throw new NumberFormatException();
                }
                return numeroValido;
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, "Ingrese un numero valido", "Datos no válidos", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private Integer solicitarEnteroValido(String mensaje) { //meotodo auxiliar lo mismo pero ahora con int
        while (true) {
            String texto = JOptionPane.showInputDialog(this, mensaje);
            if (texto == null) {
                return null;
            }
            try {
                int numero = Integer.parseInt(texto.trim());
                return numero;
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, "Debe ingresar un número entero válido.", "Datos incorrectos", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    // de aquí hacia abajo IGNORAR CODIGO ya que son metodos de la interfaz que ocupa por defecto, no borrar ni modificar.

    {
// GUI initializer generated by IntelliJ IDEA GUI Designer
// >>> IMPORTANT!! <<<
// DO NOT EDIT OR ADD ANY CODE HERE!
        $$$setupUI$$$();
    }

    /**
     * Method generated by IntelliJ IDEA GUI Designer
     * >>> IMPORTANT!! <<<
     * DO NOT edit this method OR call it in your code!
     *
     * @noinspection ALL
     */
    private void $$$setupUI$$$() {
        panelPrincipal = new JPanel();
        panelPrincipal.setLayout(new GridLayoutManager(6, 2, new Insets(5, 5, 5, 5), -1, -1, true, false));
        panelTitulo = new JPanel();
        panelTitulo.setLayout(new GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1));
        panelPrincipal.add(panelTitulo, new GridConstraints(0, 0, 1, 2, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        final JLabel label1 = new JLabel();
        label1.setEnabled(true);
        Font label1Font = this.$$$getFont$$$(null, Font.BOLD, 18, label1.getFont());
        if (label1Font != null) label1.setFont(label1Font);
        label1.setText("Sistema De Información Metereológica");
        panelTitulo.add(label1, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        panelRegistro = new JPanel();
        panelRegistro.setLayout(new GridLayoutManager(6, 1, new Insets(0, 0, 0, 0), -1, -1));
        panelPrincipal.add(panelRegistro, new GridConstraints(1, 0, 4, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        panelRegistro.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "Registro", TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, null, null));
        crearRegionButton = new JButton();
        crearRegionButton.setText("Crear region");
        panelRegistro.add(crearRegionButton, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        crearComunaButton = new JButton();
        crearComunaButton.setText("Crear comuna");
        panelRegistro.add(crearComunaButton, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        crearEstacionMeteorologicaButton = new JButton();
        crearEstacionMeteorologicaButton.setText("Crear estacion meteorologica");
        panelRegistro.add(crearEstacionMeteorologicaButton, new GridConstraints(2, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        registrarMedicionButton = new JButton();
        registrarMedicionButton.setText("Registrar medicion");
        panelRegistro.add(registrarMedicionButton, new GridConstraints(4, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        cambiarEstadoDeSensorButton = new JButton();
        cambiarEstadoDeSensorButton.setText("Cambiar estado de sensor");
        panelRegistro.add(cambiarEstadoDeSensorButton, new GridConstraints(5, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        instalarSensorButton = new JButton();
        instalarSensorButton.setText("Instalar sensor");
        panelRegistro.add(instalarSensorButton, new GridConstraints(3, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        panelIzqInferior = new JPanel();
        panelIzqInferior.setLayout(new GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1));
        panelPrincipal.add(panelIzqInferior, new GridConstraints(5, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        lblEstado = new JLabel();
        lblEstado.setText("Estado: el sistema no tiene datos registrados.");
        panelIzqInferior.add(lblEstado, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        panelDatos = new JPanel();
        panelDatos.setLayout(new GridLayoutManager(3, 1, new Insets(0, 0, 0, 0), -1, -1));
        panelPrincipal.add(panelDatos, new GridConstraints(1, 1, 3, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        panelDatos.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "Datos", TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, null, null));
        cargarDatosButton = new JButton();
        cargarDatosButton.setText("Cargar datos");
        panelDatos.add(cargarDatosButton, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        importarDatosDeTextoButton = new JButton();
        importarDatosDeTextoButton.setText("Importar datos de texto");
        panelDatos.add(importarDatosDeTextoButton, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        guardarDatosButton = new JButton();
        guardarDatosButton.setText("Guardar datos");
        panelDatos.add(guardarDatosButton, new GridConstraints(2, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        panelSalir = new JPanel();
        panelSalir.setLayout(new GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1));
        panelPrincipal.add(panelSalir, new GridConstraints(5, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        salirButton = new JButton();
        salirButton.setText("Salir");
        panelSalir.add(salirButton, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_SOUTHEAST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        panelListados = new JPanel();
        panelListados.setLayout(new GridLayoutManager(3, 1, new Insets(0, 0, 0, 0), -1, -1));
        panelPrincipal.add(panelListados, new GridConstraints(4, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        panelListados.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "Listados", TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, null, null));
        final JLabel label2 = new JLabel();
        label2.setText("Seleccione el listado:");
        panelListados.add(label2, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        cmbListados = new JComboBox();
        final DefaultComboBoxModel defaultComboBoxModel1 = new DefaultComboBoxModel();
        defaultComboBoxModel1.addElement("Seleccionar:");
        defaultComboBoxModel1.addElement("Regiones");
        defaultComboBoxModel1.addElement("Comunas");
        defaultComboBoxModel1.addElement("Estaciones de una comuna");
        defaultComboBoxModel1.addElement("Sensores de una estación");
        defaultComboBoxModel1.addElement("Mediciones de un sensor");
        defaultComboBoxModel1.addElement("Resumen estadístico de un sensor");
        defaultComboBoxModel1.addElement("Alertas de una estación");
        cmbListados.setModel(defaultComboBoxModel1);
        cmbListados.setToolTipText("seleccionar:");
        panelListados.add(cmbListados, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        generarListadoButton = new JButton();
        generarListadoButton.setText("Generar listado");
        panelListados.add(generarListadoButton, new GridConstraints(2, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
    }

    /**
     * @noinspection ALL
     */
    private Font $$$getFont$$$(String fontName, int style, int size, Font currentFont) {
        if (currentFont == null) return null;
        String resultName;
        if (fontName == null) {
            resultName = currentFont.getName();
        } else {
            Font testFont = new Font(fontName, Font.PLAIN, 10);
            if (testFont.canDisplay('a') && testFont.canDisplay('1')) {
                resultName = fontName;
            } else {
                resultName = currentFont.getName();
            }
        }
        Font font = new Font(resultName, style >= 0 ? style : currentFont.getStyle(), size >= 0 ? size : currentFont.getSize());
        boolean isMac = System.getProperty("os.name", "").toLowerCase(Locale.ENGLISH).startsWith("mac");
        Font fontWithFallback = isMac ? new Font(font.getFamily(), font.getStyle(), font.getSize()) : new StyleContext().getFont(font.getFamily(), font.getStyle(), font.getSize());
        return fontWithFallback instanceof FontUIResource ? fontWithFallback : new FontUIResource(fontWithFallback);
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$() {
        return panelPrincipal;
    }

}
