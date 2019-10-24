/*
 * Copyright 2019 Square Inc.
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
package com.squareup.workflow.ui

import com.google.common.truth.Truth.assertThat
import org.junit.Test

class HintsTest {
  private object StringHint : HintKey<String>(String::class) {
    override val default = ""
  }

  private object OtherStringHint : HintKey<String>(String::class) {
    override val default = ""
  }

  private data class DataHint(
    val int: Int = -1,
    val string: String = ""
  ) {
    companion object : HintKey<DataHint>(DataHint::class) {
      override val default = DataHint()
    }
  }

  @Test fun defaults() {
    assertThat(Hints()[DataHint]).isEqualTo(DataHint())
  }

  @Test fun put() {
    val hints = Hints() +
        (StringHint to "fnord") +
        (DataHint to DataHint(42, "foo"))

    assertThat(hints[StringHint]).isEqualTo("fnord")
    assertThat(hints[DataHint]).isEqualTo(DataHint(42, "foo"))
  }

  @Test fun `map equality`() {
    val hints1 = Hints() +
        (StringHint to "fnord") +
        (DataHint to DataHint(42, "foo"))

    val hints2 = Hints() +
        (StringHint to "fnord") +
        (DataHint to DataHint(42, "foo"))

    assertThat(hints1).isEqualTo(hints2)
  }

  @Test fun `map inequality`() {
    val hints1 = Hints() +
        (StringHint to "fnord") +
        (DataHint to DataHint(42, "foo"))

    val hints2 = Hints() +
        (StringHint to "fnord") +
        (DataHint to DataHint(43, "foo"))

    assertThat(hints1).isNotEqualTo(hints2)
  }

  @Test fun `key equality`() {
    assertThat(StringHint).isEqualTo(StringHint)
  }

  @Test fun `key inequality`() {
    assertThat(StringHint).isNotEqualTo(OtherStringHint)
  }

  @Test fun override() {
    val hints = Hints() +
        (StringHint to "able") +
        (StringHint to "baker")

    assertThat(hints[StringHint]).isEqualTo("baker")
  }

  @Test fun `keys of the same type`() {
    val hints = Hints() +
        (StringHint to "able") +
        (OtherStringHint to "baker")

    assertThat(hints[StringHint]).isEqualTo("able")
    assertThat(hints[OtherStringHint]).isEqualTo("baker")
  }
}
