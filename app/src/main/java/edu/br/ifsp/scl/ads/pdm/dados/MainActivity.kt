package edu.br.ifsp.scl.ads.pdm.dados

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import edu.br.ifsp.scl.ads.pdm.dados.databinding.ActivityMainBinding
import kotlin.random.Random
import kotlin.random.nextInt

class MainActivity : AppCompatActivity() {

    //instancia de referencia para a classe de ligacao com os componentes graficos
    private lateinit var activityMainBinding: ActivityMainBinding

    // gerador de numeros randomicos
    private lateinit var geradorRandomico: Random

    private lateinit var settingsActivityLaucher: ActivityResultLauncher<Intent>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        activityMainBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(activityMainBinding.root)

        geradorRandomico = Random(System.currentTimeMillis())

        activityMainBinding.jogarDadoBt.setOnClickListener {
            val resultado: Int = geradorRandomico.nextInt(1..6)
            // primeiro constroi a string e depois aplico o corpo a string referenciada por it
            "A face sorteada foi $resultado".also { activityMainBinding.resultadoTv.text = it }
            val nomeImagem = "dice_${resultado}"
            activityMainBinding.resultadoIv.setImageResource(
                resources.getIdentifier(nomeImagem, "mipmap", packageName)
            )
            activityMainBinding.resultadoIv.visibility = View.VISIBLE
        }

        settingsActivityLaucher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if(result.resultCode == RESULT_OK){
                // modificações na minha view
                if(result.data != null){
                    val configuracao: Configuracao? = result.data?.getParcelableExtra<Configuracao>(Intent.EXTRA_USER)

                    // exercicio, modificar a view a partir dos dados fornecido pela configuracao

                    if (configuracao != null) {

                        activityMainBinding.jogarDadoBt.setOnClickListener {

                            val resultado: Int = gerarNumero(configuracao)
                            val resultado2: Int = gerarNumero(configuracao)
                            val nomeImagem = "dice_${resultado}"
                            val nomeImagem2 = "dice_${resultado2}"

                           if(configuracao.numeroFaces <= 6 && configuracao.numeroDados == 1) {
                               // primeiro constroi a string e depois aplico o corpo a string referenciada por it
                               gerarFraseSingular(resultado).also {
                                   activityMainBinding.resultadoTv.text = it
                               }

                               activityMainBinding.resultadoIv.setImageResource(
                                   resources.getIdentifier(nomeImagem, "mipmap", packageName)
                               )

                               activityMainBinding.resultadoIv.visibility = View.VISIBLE
                               activityMainBinding.resultado2Iv.visibility = View.GONE
                           }
                            if(configuracao.numeroFaces <= 6 && configuracao.numeroDados == 2){
                                // mostra a segunda view
                                gerarFrasePlural(resultado, resultado2).also {
                                    activityMainBinding.resultadoTv.text = it
                                }

                                activityMainBinding.resultadoIv.setImageResource(
                                    resources.getIdentifier(nomeImagem, "mipmap", packageName)
                                )

                                activityMainBinding.resultado2Iv.setImageResource(
                                    resources.getIdentifier(nomeImagem2, "mipmap", packageName)
                                )

                                // mostro as imageView
                                activityMainBinding.resultado2Iv.visibility = View.VISIBLE
                                activityMainBinding.resultadoIv.visibility = View.VISIBLE
                            }
                            // se faces maior que 6 = não mostrar imagens, só a mensagem
                            else if(configuracao.numeroFaces > 6) {

                                // removo as imageView
                                activityMainBinding.resultadoIv.visibility = View.GONE
                                activityMainBinding.resultado2Iv.visibility = View.GONE

                                if(configuracao.numeroDados == 2){
                                    gerarFrasePlural(resultado, resultado2).also {
                                        activityMainBinding.resultadoTv.text = it
                                    }
                                } else {
                                    gerarFraseSingular(resultado).also {
                                        activityMainBinding.resultadoTv.text = it
                                    }
                                }
                            }
                        }

                    }

                }

            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(item.itemId == R.id.settingsMi){
            val settingsIntent = Intent(this, SettingsActivity::class.java)
            settingsActivityLaucher.launch(settingsIntent)
            return true
        }
        return false
    }

    private fun gerarNumero(configuracao: Configuracao): Int {
        var resultado: Int = geradorRandomico.nextInt(1..configuracao.numeroFaces)
        return resultado
    }

    private fun gerarFraseSingular(resultado: Int): String{
        return "A face sorteada foi $resultado"
    }

    private fun gerarFrasePlural(resultado: Int, resultado2: Int): String{
        return "As faces sorteadas foram $resultado e $resultado2"
    }
}