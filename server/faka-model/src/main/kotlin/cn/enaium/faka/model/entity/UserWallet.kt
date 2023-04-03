/*
 * Copyright 2023 Enaium
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

package cn.enaium.faka.model.entity

import org.babyfish.jimmer.sql.*
import org.babyfish.jimmer.sql.meta.UUIDIdGenerator
import java.math.BigDecimal
import java.time.LocalDateTime
import java.util.*
import javax.validation.constraints.Max

/**
 * @author Enaium
 */
@Entity
@Table(name = "t_user_wallet")
interface UserWallet {
    /**
     * 主键
     */
    @Id
    @GeneratedValue(generatorType = UUIDIdGenerator::class)
    val id: UUID

    /**
     * 用户ID
     */
    @Key
    @Column(name = "user_id")
    val userId: UUID

    /**
     * 用户
     */
    @OneToOne
    val user: User

    /**
     *  余额
     */
    @get:Max(value = 999999999)
    val balance: BigDecimal
}