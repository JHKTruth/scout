/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package org.jhk.pulsing.spark.job

import org.apache.spark.SparkConf
import org.apache.spark.SparkContext
import org.apache.spark.SparkContext._

import org.jhk.pulsing.shared.util.CommonConstants._

/**
 * Initial thought is to allow add location feature in the project and allow ranking of 
 * the location by the user. Then this job will compute the rank for the location.
 * 
 * Additionally maybe just to play around allow tagging of the location and then gather 
 * rank for the tags and do additional mappings as well.
 * 
 * @author Ji Kim
 */
class LocationRank {
  
  def main(args: Array[String]): Unit = {
    val configuration = new SparkConf().setMaster(PROJECT_POINT).setAppName(APP_NAME)
    val sparkContext = new SparkContext(configuration);
  }
}
