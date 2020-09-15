/*
 * Copyright 2002-2018 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.imlehr.summer.core.utils;

import java.util.List;

/**
 * 仿照Spring的Assert工具类写的
 * todo 但是我不知道为什么源码要把这里设计为抽象类
 */
public abstract class Assert {


	public static void notEmpty(Object[] array, String message) {
		if (array.length==0) {
			throw new IllegalArgumentException(message);
		}
	}

	public static boolean NotEmpty(List list)
	{
		return (list!=null && !list.isEmpty());
	}

}
