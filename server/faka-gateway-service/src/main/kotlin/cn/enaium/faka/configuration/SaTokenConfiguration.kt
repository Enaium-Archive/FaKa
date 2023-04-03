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

package cn.enaium.faka.configuration

import cn.dev33.satoken.`fun`.SaParamFunction
import cn.dev33.satoken.reactor.filter.SaReactorFilter
import cn.dev33.satoken.router.SaRouter
import cn.dev33.satoken.stp.StpUtil
import cn.enaium.faka.result.Result
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

/**
 * @author Enaium
 */
@Configuration
class SaTokenConfiguration(val unmatchedConfiguration: UnmatchedConfiguration) {
    @Bean
    fun saReactorFilter(): SaReactorFilter {
        return object : SaReactorFilter() {}.addInclude("/**")
            .setAuth {
                SaRouter.match("/**").notMatchMethod("OPTIONS")
                    .check(SaParamFunction {
                        if (unmatchedConfiguration.all.none {
                                SaRouter.isMatchCurrMethod(arrayOf(it.method)) && SaRouter.isMatchCurrURI(
                                    it.pattern
                                )
                            }) {
                            StpUtil.checkLogin()
                        }
                    })
            }.setError {
                jacksonObjectMapper().writeValueAsString(
                    Result.Builder.fail<Any>(
                        status = Result.Status.NOT_LOGIN,
                        message = it.message ?: Result.Status.NOT_LOGIN.message
                    )
                )
            }
    }
}