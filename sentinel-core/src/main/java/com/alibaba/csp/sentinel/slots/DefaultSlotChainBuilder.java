/*
 * Copyright 1999-2018 Alibaba Group Holding Ltd.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.alibaba.csp.sentinel.slots;

import com.alibaba.csp.sentinel.slotchain.DefaultProcessorSlotChain;
import com.alibaba.csp.sentinel.slotchain.ProcessorSlotChain;
import com.alibaba.csp.sentinel.slotchain.SlotChainBuilder;
import com.alibaba.csp.sentinel.slots.block.authority.AuthoritySlot;
import com.alibaba.csp.sentinel.slots.block.degrade.DegradeSlot;
import com.alibaba.csp.sentinel.slots.block.flow.FlowSlot;
import com.alibaba.csp.sentinel.slots.clusterbuilder.ClusterBuilderSlot;
import com.alibaba.csp.sentinel.slots.logger.LogSlot;
import com.alibaba.csp.sentinel.slots.nodeselector.NodeSelectorSlot;
import com.alibaba.csp.sentinel.slots.statistic.StatisticSlot;
import com.alibaba.csp.sentinel.slots.system.SystemSlot;

/**
 * Builder for a default {@link ProcessorSlotChain}.
 *
 * @author qinan.qn
 * @author leyou
 */
public class DefaultSlotChainBuilder implements SlotChainBuilder {

    @Override
    public ProcessorSlotChain build() {
        ProcessorSlotChain chain = new DefaultProcessorSlotChain();
        /**
         * 构建context下面的node节点用于统计context资源
         */
        chain.addLast(new NodeSelectorSlot());
        /**
         * resource 全局资源统计
         */
        chain.addLast(new ClusterBuilderSlot());
        /**
         * 日志log
         */
        chain.addLast(new LogSlot());

        /**
         *
         */
        chain.addLast(new StatisticSlot());
        /**
         * 黑白名单校验， 用于校验请求来源
         */
        chain.addLast(new AuthoritySlot());
        /**
         * 系统规则校验， 用于校验当前系统状态是否超过了用户配置的阈值
         */
        chain.addLast(new SystemSlot());
        /**
         * 限流检查
         */
        chain.addLast(new FlowSlot());
        /**
         * 降级检查
         */
        chain.addLast(new DegradeSlot());

        return chain;
    }

}
