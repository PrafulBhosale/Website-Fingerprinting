import org.apache.spark.mllib.linalg.Vectors
import org.apache.spark.mllib.regression.LabeledPoint
import org.apache.spark.SparkContext
import org.apache.spark.streaming._
import org.apache.spark.streaming.StreamingContext._
import org.apache.spark.streaming.kafka._
import org.apache.spark.SparkConf
import java.lang._
import java.io.File
import org.apache.spark.mllib.tree.RandomForest
import org.apache.spark.mllib.tree.model.RandomForestModel
import org.apache.spark.mllib.tree.configuration.Strategy
import org.apache.spark.mllib.util.MLUtils
import org.apache.spark.mllib.feature.Normalizer
import org.apache.spark.mllib.util.MLUtils

/**
 * @author Group No. 4
 */
object createModelRF {
  
  def main(args: Array[String]) {
    
    val sparkConf = new SparkConf().setAppName("KafkaRandomForest")
    sparkConf.setMaster("local[2]")
    val sc = new SparkContext(sparkConf)
   
    val Array(finalPathAddress) = args
    val data = sc.textFile("/Users/Ishan/Desktop/BigData/Project/code/training_data/train2")
    
     val trainData = data.map { x =>
       val splitParts = x.split(',')
       LabeledPoint(splitParts(0).toCharArray()(7).toDouble-48, Vectors.dense(splitParts(1).split(' ').map(_.toDouble).take(873)))
     }
    val normalizer = new Normalizer()
    val normalizedData = trainData.map(x => LabeledPoint(x.label, normalizer.transform(x.features)))
    val numClasses = 5
    val categoricalFeaturesInfo = Map[Int, Int]()
    val numTrees = 10 // Use more in practice.
    val featureSubsetStrategy = "auto" // Let the algorithm choose.
    val impurity = "gini"
    val maxDepth = 30
    val maxBins = 32
    
    val RFmodel = RandomForest.trainClassifier(normalizedData, numClasses, categoricalFeaturesInfo,
      numTrees, featureSubsetStrategy, impurity, maxDepth, maxBins)
      RFmodel.save(sc, finalPathAddress)
    
    // Train a RandomForest model.
   /*val treeStrategy = Strategy.defaultStrategy("Classification")
    val numTrees = 3 // Use more in practice.
    val featureSubsetStrategy = "auto" // Let the algorithm choose.
    val model = RandomForest.trainClassifier(parsedData,
      treeStrategy, numTrees, featureSubsetStrategy, seed = 12345)
    */
    //ssc.start()
    //ssc.awaitTermination()
    

  }

}
