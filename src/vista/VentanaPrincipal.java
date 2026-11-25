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
import java.util.Locale;

public class VentanaPrincipal extends JFrame {
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
    private JComboBox comboBox1;
    private JButton generarListadoButton;
    private JPanel panelRegistro;
    private JPanel panelDatos;
    private JPanel panelIzqInferior;
    private JLabel lblEstado;
    private JPanel panelTitulo;

    public VentanaPrincipal() {

        setTitle("Sistema de Información Meteorológica");

        setContentPane(panelPrincipal);

        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);

        pack();

        setLocationRelativeTo(null);
        salirButton.addActionListener(e -> dispose());
        crearRegionButton.addActionListener(e -> crearRegion());
        crearComunaButton.addActionListener(e -> crearComuna());
        crearEstacionMeteorologicaButton.addActionListener(e -> crearEstacionMeteorologica());
        cargarDatosButton.addActionListener(e -> gestionarDatos("CARGAR"));
        importarDatosDeTextoButton.addActionListener(e -> gestionarDatos("IMPORTAR"));
        guardarDatosButton.addActionListener(e -> gestionarDatos("GUARDAR"));
    }
    private void crearRegion(){
        String codigoTexto = JOptionPane.showInputDialog(this, "Ingrese el codigo de la region");
        if(codigoTexto == null){
            return;
        }
        String nombreRegion = JOptionPane.showInputDialog(this, "Ingrese el nombre de la region");
        if(nombreRegion == null) {
            return;
        }
        try{
            InstitutoMeteorologia.getInstance().creaRegion(Integer.parseInt(codigoTexto), nombreRegion);
            JOptionPane.showMessageDialog(this, "Region creada con exito", "Operacion Exitosa",JOptionPane.INFORMATION_MESSAGE);
        } catch (MeteorologiaException e) {
            JOptionPane.showMessageDialog(this, e.getMessage(), "Operacion fallida",JOptionPane.ERROR_MESSAGE);
        }
    }
    private void crearComuna(){
        String codigoString = JOptionPane.showInputDialog(this, "Ingrese el codigo de la comuna");
        if(codigoString == null){
            return;
        }
        String nombreComuna = JOptionPane.showInputDialog(this, "Ingrese el nombre de la comuna");
        if(nombreComuna == null){
            return;
        }
        String codigoRegionString = JOptionPane.showInputDialog(this, "Ingrese el codigo de la region");
        if(codigoRegionString == null){
            return;
        }
        try{
            InstitutoMeteorologia.getInstance().creaComuna(Integer.parseInt(codigoString), nombreComuna, Integer.parseInt(codigoRegionString));
            JOptionPane.showMessageDialog(this, "Comuna creada con éxito", "Operacion éxitosa", JOptionPane.INFORMATION_MESSAGE);
        }catch(MeteorologiaException e) {
            JOptionPane.showMessageDialog(this, e.getMessage(), "Operación fallida", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void crearEstacionMeteorologica(){
        String cod = JOptionPane.showInputDialog(this, "Ingrese el codigo de la estación");
        if(cod == null){
            return;
        }
        String nombre = JOptionPane.showInputDialog(this, "Ingrese el nombre de la estación");
        if(nombre == null){
            return;
        }
        Float lon = solicitarFloatValido("Ingrese la longitud");
        if(lon == null){
            return;
        }
        Float lat = solicitarFloatValido("Ingrese la latitud");
        if(lat == null){
            return;
        }
        Float alt = solicitarFloatValido("Ingrese la altitud");
        if(alt == null){
            return;
        }
        Integer codRegion = solicitarEnteroValido("Ingrese el codigo de la region");
        if(codRegion == null){
            return;
        }
        Integer codComuna = solicitarEnteroValido("Ingrese el codigo de la comuna");
        if(codComuna == null){
            return;
        }
        try{
            InstitutoMeteorologia.getInstance().creaEstacion(cod, nombre, lon, lat, alt, codRegion, codComuna);
            JOptionPane.showMessageDialog(this, "Estación creada con éxito", "Operación exitosa", JOptionPane.INFORMATION_MESSAGE);
        } catch (MeteorologiaException e) {
            JOptionPane.showMessageDialog(this, e.getMessage(), "Operación fallida", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void gestionarDatos(String tipoOperacion){//hice un switch para simplificar todo lo de los datos en una funcion
        try{
            switch(tipoOperacion){
                case "CARGAR" -> {InstitutoMeteorologia.getInstance().cargaDatos();
                JOptionPane.showMessageDialog(this, "Datos cargados correctamente", "Operacion éxitosa", JOptionPane.INFORMATION_MESSAGE);}

                case "GUARDAR" -> {InstitutoMeteorologia.getInstance().guardaDatos();
                JOptionPane.showMessageDialog(this, "Datos guardados correctamente", "Operacion éxitosa", JOptionPane.INFORMATION_MESSAGE);}

                case "IMPORTAR" -> {InstitutoMeteorologia.getInstance().importaDatosTexto();
                JOptionPane.showMessageDialog(this, "Datos de texto importados correctamente", "Operacion éxitosa", JOptionPane.INFORMATION_MESSAGE);}
            }
        }catch (MeteorologiaException e){
            JOptionPane.showMessageDialog(this, e.getMessage(), "Operación fallida", JOptionPane.ERROR_MESSAGE);
        }
    }

    private Float solicitarFloatValido(String mensaje){//Metodo auxiliar para crearEstacion Meteorologica ya que se solicitan float con los JOpane que leen String.
        while(true){
            String texto = JOptionPane.showInputDialog(this, mensaje);
            if(texto == null){
                return null; //El usuario puso cancelar, por ende salimos con null
            }
            try{
                float numeroValido = Float.parseFloat(texto.trim().replace(',','.'));//el replace no me lo sabia asi que le pregunte a chatgpt
                if(!Float.isFinite(numeroValido)){
                    throw new NumberFormatException();
                }
                return numeroValido;
            }catch(NumberFormatException e){
                JOptionPane.showMessageDialog(this, "Ingrese un numero valido","Datos no válidos", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    private Integer solicitarEnteroValido(String mensaje){ //meotodo auxiliar lo mismo pero ahora con int
        while(true) {
            String texto = JOptionPane.showInputDialog(this, mensaje);
            if (texto == null){
                return null;
            }
            try {
                int numero = Integer.parseInt(texto.trim());
                return numero;
            }catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this,"Debe ingresar un número entero válido.","Datos incorrectos", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

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
        comboBox1 = new JComboBox();
        final DefaultComboBoxModel defaultComboBoxModel1 = new DefaultComboBoxModel();
        comboBox1.setModel(defaultComboBoxModel1);
        panelListados.add(comboBox1, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
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
