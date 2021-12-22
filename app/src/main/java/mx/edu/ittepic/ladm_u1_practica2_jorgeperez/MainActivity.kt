package mx.edu.ittepic.ladm_u1_practica2_jorgeperez

import android.content.Context
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.widget.RadioGroup
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import kotlinx.android.synthetic.main.activity_main.*
import java.io.*
import java.util.jar.Manifest

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Botón de permisos
        button3.setOnClickListener {
            if(ContextCompat.checkSelfPermission(this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE)==PackageManager.PERMISSION_DENIED){
                // SI ENTRA EN ESTE IF, ES PORQUE NO TIENE LOS PERMISOS
                // EL SIGUIENTE CÓDIGO SOLICITA LOS PERMISOS
                ActivityCompat.requestPermissions(this,
                    arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE,android.Manifest.permission.WRITE_EXTERNAL_STORAGE),0)
            }else{
                mensaje("PERMISOS YA OTORGADOS")
            }
        }

        // Botón guardar
        button.setOnClickListener {
            // Sino selecciona ningún radiobutton, nos detenemos en el proceso
            if(!radioButton.isChecked && !radioButton2.isChecked){
                mensaje("SELECCIONE UNA OPCIÓN EN LOS RADIO BOTONES: ARCHIVO INTERNO O EXTERNO")
                return@setOnClickListener
            }
            // Guardar archivo en memoria interna
            else if(radioButton.isChecked){
                guardarArchivoInterno()
                return@setOnClickListener
            }
            // Guardar archivo en memoria externa
            else if(radioButton2.isChecked){
                guardarArchivoSD()
                return@setOnClickListener
            }
        }

        // Botón leer
        button2.setOnClickListener {
            // Sino selecciona ningún radiobutton, nos detenemos en el proceso
            if(!radioButton.isChecked && !radioButton2.isChecked){
                mensaje("SELECCIONE UNA OPCIÓN EN LOS RADIO BOTONES: ARCHIVO INTERNO O EXTERNO")
                return@setOnClickListener
            }
            // Guardar archivo en memoria interna
            else if(radioButton.isChecked){
                leerArchivoInterno()
                return@setOnClickListener
            }
            // Guardar archivo en memoria externa
            else if(radioButton2.isChecked){
                leerArchivoSD()
                return@setOnClickListener
            }
        }
    }

    // Función para leer el archivo desde la SD
    fun leerArchivoSD(){
        var nameArchivo = editText3.text.toString()
        // Si no hay memoria SD, no continuamos con el proceso
        if(noSD()){
            mensaje("NO HAY MEMORIA EXTERNA")
            return
        }
        // Si sí hay memoria SD, continuamos con el proceso
        try {
            var rutaSD = Environment.getExternalStorageDirectory()
            var datosArchivo = File(rutaSD.absolutePath,nameArchivo)

            var flujoEntrada = BufferedReader(InputStreamReader(FileInputStream(datosArchivo)))

            var data = flujoEntrada.readLine()

            ponerTexto(data)
            flujoEntrada.close()
        }catch (error : IOException){
            mensaje(error.message.toString())
        }
    }

    // Función para guardar el archivo en SD
    fun guardarArchivoSD(){
        var nameArchivo = editText3.text.toString()
        // Si no hay memoria SD, no continuamos con el proceso
        if(noSD()){
            mensaje("NO HAY MEMORIA EXTERNA SD")
            return
        }
        // Si tiene un amemoria SD, procedemos a guardar el archivo
        try {
            var rutaSD = Environment.getExternalStorageDirectory()
            var datosArchivo = File(rutaSD.absolutePath, nameArchivo)

            var flujoSalida = OutputStreamWriter(FileOutputStream(datosArchivo))
            var data = editText2.text.toString()

            flujoSalida.write(data)
            flujoSalida.flush()
            flujoSalida.close()

            mensaje("¡ÉXITO! Se guardó el archivo correctamente en MEMORIA EXTERNA SD")
            ponerTexto("")
        }catch (error : IOException){
            mensaje(error.message.toString())
        }
    }

    // Verificar que se tenga una memoria SD
    fun noSD() : Boolean{
        var estado = Environment.getExternalStorageState()
        if(estado != Environment.MEDIA_MOUNTED){
            return true
        }
        return false
    }

    // Función para guardar un archivo interno
    fun guardarArchivoInterno(){
        var nameArchivo = editText3.text.toString()
        try {
            var flujoSalida = OutputStreamWriter(openFileOutput(nameArchivo,Context.MODE_PRIVATE))
            var data = editText2.text.toString()

            flujoSalida.write(data)
            flujoSalida.flush()
            flujoSalida.close()

            mensaje("¡ÉXITO! Se guardó el archivo correctamente en MEMORIA INTERNA")
            ponerTexto("")
        }catch (error : IOException){
            mensaje(error.message.toString())
        }
    }

    //Función para leer un archivo interno
    private fun leerArchivoInterno(){
        var nameArchivo = editText3.text.toString()
        try {
            var flujoEntrada = BufferedReader(InputStreamReader(openFileInput(nameArchivo)))

            var data = flujoEntrada.readLine()

            ponerTexto(data)
            flujoEntrada.close()
        }catch (error : IOException){
            mensaje(error.message.toString())
        }
    }
    
    // Función para mostrar un mensaje
    fun mensaje(m:String){
        AlertDialog.Builder(this)
            .setTitle("ATENCIÓN")
            .setMessage((m))
            .setPositiveButton("OK"){d,i->}
            .show()
        
    }
    
    // Función para poner texto en el multiline
    fun ponerTexto(tex:String){
        editText2.setText(tex)
    }
}
