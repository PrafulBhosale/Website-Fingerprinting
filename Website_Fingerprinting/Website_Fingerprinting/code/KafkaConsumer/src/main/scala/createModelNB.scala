import org.apache.spark.mllib.classification.{NaiveBayes, NaiveBayesModel}
import org.apache.spark.mllib.linalg.Vectors
import org.apache.spark.mllib.regression.LabeledPoint
import org.apache.spark.SparkContext
import org.apache.spark.streaming._
import org.apache.spark.streaming.StreamingContext._
import org.apache.spark.streaming.kafka._
import org.apache.spark.SparkConf
import java.lang._
import java.io.File
import org.apache.spark.mllib.feature.Normalizer
import org.apache.spark.mllib.util.MLUtils

/**
 * @author Group No. 4
 */
object createModelNB {
  
  def main(args: Array[String]) {
    val sparkConf=new SparkConf().setAppName("KafkaNaiveBayes")
    sparkConf.setMaster("local[2]")
    val sc=new SparkContext(sparkConf)
    
    val Array(finalPathAddress) = args
    val data=sc.textFile("/Users/Ishan/Desktop/BigData/Project/code/training_data/train2")
    val normalizer = new Normalizer()
    val trainData = data.map { x =>
       val splitParts = x.split(',')
       LabeledPoint(splitParts(0).toCharArray()(7).toDouble-48, Vectors.dense(splitParts(1).split(' ').map(_.toDouble).take(873)))
     }
    
    val normalizedData = trainData.map(x => LabeledPoint(x.label, normalizer.transform(x.features)))
    
    val NBmodel = NaiveBayes.train(normalizedData, lambda = 1.0, modelType = "multinomial")
    NBmodel.save(sc, finalPathAddress)
   
  }

}
