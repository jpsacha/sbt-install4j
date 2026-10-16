/*
 * Copyright 2014-2026 Jarek Sacha (jpsacha -at- gmail.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package net.ij_plugins.sf.sbt.install4j

import sbt.*
import xsbti.{FileConverter, HashedVirtualFileRef}

import java.io.File

/** Differences between sbt 1 and sbt 2 APIs, sbt 2 version. Same API is in `scala-2.12/PluginCompat.scala`. */
private[install4j] object PluginCompat {

  /** Classpath entry. */
  type FileRef = HashedVirtualFileRef

  def toFiles(classpath: Seq[Attributed[FileRef]])(implicit conv: FileConverter): Seq[File] =
    classpath.map(a => conv.toPath(a.data).toFile)
}
