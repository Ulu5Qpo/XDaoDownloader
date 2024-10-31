package com.example.xddemo.data

import com.example.xddemo.data.model.ReplyEntity
import com.example.xddemo.data.model.ThreadEntity


data class Message(val author: String, val body: String)

val mockThread = ThreadEntity(
    id = 1,
    replyCount = 114,
    img = "",
    ext = "",
    now = "2022-12-15",
    userHash = "c137reed",
    name = "",
    title = "",
    content = "我，我想起来了\uD83D\uDE26我全都想起来了\uD83D\uDE27我不是大剑侠，不是太刀侠，我，我是，我是斩斧侠！！！斩斧，我真的好喜欢你啊，mua，为了你，我要舍弃真蓄舍弃铁山靠舍弃居合舍弃登龙！我要，我要……我要他x的磁场转动！！！一百万匹力量！！！零距离！属！性！解！放！！！！",
)

val mockThreads = listOf(
    ThreadEntity(
        id = 1,
        replyCount = 114,
        img = "",
        ext = "",
        now = "2022-12-15",
        userHash = "c137reed",
        name = "",
        title = "",
        content = "我，我想起来了\uD83D\uDE26我全都想起来了\uD83D\uDE27我不是大剑侠，不是太刀侠，我，我是，我是斩斧侠！！！斩斧，我真的好喜欢你啊，mua，为了你，我要舍弃真蓄舍弃铁山靠舍弃居合舍弃登龙！我要，我要……我要他x的磁场转动！！！一百万匹力量！！！零距离！属！性！解！放！！！！",
    ),
    ThreadEntity(
        id = 1,
        replyCount = 114,
        img = "",
        ext = "",
        now = "2022-12-15",
        userHash = "c137reed",
        name = "",
        title = "",
        content = "承受他的激光有多难熬\uD83D\uDE21我比你更懂\uD83D\uDE2D\n" +
                "纳！\uD83D\uDE21\uD83D\uDE21\n" +
                "小猴子，你果然有几分他的影子，既然如此我也不再留手，得罪了\uD83D\uDE04\uD83D\uDE04！\n" +
                "今日替他\uD83D\uDC48教教后人\uD83E\uDD32\n" +
                "躲不过这登，还不如再来过\uD83D\uDE15\n" +
                "不知为何，一见到你\uD83D\uDE29我就想起很多事情\uD83D\uDE2D\uD83D\uDE2D\uD83D\uDE2D\n" +
                "你这猴子真令我欢喜\uD83D\uDE06\n" +
                "野图那些庸才\uD83D\uDE21不敌我者多\uD83D\uDC49胜似我者少\uD83D\uDC48从前他在时\uD83E\uDD27就只有他\uD83D\uDE0B值得一战\n" +
                "打开生死路\uD83D\uDE21让我好生羡慕\n" +
                "我的黑龙歼灭刀有些钝了☝\uD83E\uDD13正好帮我开开刃\uD83D\uDE21\n" +
                "你想接他衣钵，这条路……太苦了\uD83D\uDE2B\n" +
                "当年他被押去斗技场，刀砍斧解，棍戳剑劈，毫发无伤，如今他用自己换回了你来……\uD83D\uDE2D\n" +
                "让我试试\uD83D\uDC46你和他到底有何不同！\uD83D\uDE4C\n" +
                "你，终究不是他\uD83D\uDE1E☝\n" +
                "此番想见，是喜呀\uD83D\uDE0F\n" +
                "你不在的每一天，我都在想你\uD83D\uDE18\n" +
                "撑不过去，那也不配为黄金奖杯\uD83D\uDE4F\n" +
                "斗气硬化！果真\uD83D\uDC35是个好猴子！☝\n" +
                "既然帮你，便帮到底罢\uD83D\uDC50\n" +
                "太刀，杀到！\uD83D\uDC7F\uD83D\uDD2A\n" +
                "若敌不住，就教杀了\uD83D\uDC80\n" +
                "太奋了，与你斗……还是这般费气掉刃\uD83D\uDE29\uD83D\uDE29\n" +
                "红手，就得……硬！\uD83D\uDE13\uD83D\uDC4A\uD83D\uDE21\n" +
                "登死了，一毛不值，挺得住，才配做……黄金奖杯！\uD83D\uDC4F\uD83D\uDE21\n" +
                "现在有十分像你了！\uD83D\uDE04\uD83D\uDC4D\n" +
                "是他赢了……我遣众天尊一路试你，就是为了这一刻\uD83D\uDE2D\n" +
                "那一战后，我今日才明白……\uD83D\uDE22\n" +
                "麻陷阱，锁不住他\uD83D\uDFE8\uD83D\uDFE8\uD83D\uDFE8\n" +
                "落陷阱，捆不住他⬛\uFE0F⬛\uFE0F⬛\uFE0F\n" +
                "但愿你，不会辜负他\uD83D\uDE11！",
    ),

)

val mockReply = ReplyEntity(
    id = 114514,
    threadId = 1,
    img = "test",
    ext = "",
    now = "2022-12-15",
    userHash = "c137reed",
    name = "",
    title = "",
    content = "最后说一次，「斩斧」要「属性解放」了。\uD83D\uDE21\n" +
            "『属性解放』！\n" +
            "『高速变形』\n" +
            "『金刚连斧』\n" +
            "『属性装填反击』\n" +
            "『零距离属性解放突刺』\n" +
            "『压缩属性解放终结』\n" +
            "『飞翔龙剑』\n" +
            "『铁丝虫步伐』\n" +
            "『变形连斩』\n" +
            "『跳跃属性解放刺』\n" +
            "『二连回旋斩』\n" +
            "『上捞斩』\n" +
            "『突进直斩』\n" +
            "『剑鬼形态』\n" +
            "『暴风之斧』\n" +
            "话已至此，\n" +
            "『属性解放』！"
)

val mockReplies = listOf(
    ReplyEntity(
        id = 114514,
        threadId = 1,
        img = "",
        ext = "",
        now = "2022-12-15",
        userHash = "c137reed",
        name = "",
        title = "",
        "太刀了"
    ),
    ReplyEntity(
        id = 114514,
        threadId = 1,
        img = "",
        ext = "",
        now = "2022-12-15",
        userHash = "c137reed",
        name = "",
        title = "",
        "太刀了\uD83D\uDE2D铁刀了\uD83D\uDE2D骨刀了\uD83D\uDE2D土砂龙太刀了\uD83D\uDE2D深土太刀了\uD83D\uDE2D优雅弯刀了\uD83D\uDE2D飞龙刀了\uD83D\uDE2D防卫队连刃式太刀了\uD83D\uDE2D幻光刃了\uD83D\uDE2D蛮鄂龙激牙了\uD83D\uDE2D巢穴搭档了\uD83D\uDE2D崩坏雷颚了\uD83D\uDE2D朱赤麻痹牙了\uD83D\uDE2D碎龙偃月刀了\uD83D\uDE2D仙子冰川了\uD83D\uDE2D夜刀月影了\uD83D\uDE2D亚铎霜锋了\uD83D\uDE2D天上天下天地无双刀了\uD83D\uDE2D超采掘十字镐了\uD83D\uDE2D狱刀龙骨了\uD83D\uDE2D冰灵芒刃了\uD83D\uDE2D真苍星的太刀舞龙了\uD83D\uDE2D飞龙刀月了\uD83D\uDE2D暴君柱头了\uD83D\uDE2D行云流水和光了\uD83D\uDE2D鬼神薙刀了\uD83D\uDE2D碎光之晓刀了\uD83D\uDE2D漆黑爪终焉了\uD83D\uDE2D黑龙歼灭刀了\uD83D\uDE0B总而言之就是太刀了\uD83D\uDE2D"
    ),
    ReplyEntity(
        id = 114514,
        threadId = 1,
        img = "",
        ext = "",
        now = "2022-12-15",
        userHash = "c137reed",
        name = "",
        title = "",
        "玩大剑吧，大剑是谁也无法拒绝的武器！\uD83D\uDE21我很佩服第一个停龙车的人，因为说不定龙会突然回头\uD83D\uDE21\n" +
                "人是一定要玩大剑的！\uD83D\uDE21簇拥我上天堂的，正是暴力的武器大剑啊\uD83D\uDC4C！巨型太刀啊，你的\uD83E\uDD90头正是你的弱点！\uD83D\uDE06\n" +
                "大剑，会指引我到达天堂！\uD83E\uDD70\n" +
                "我感觉到了，我找到位置了，嗯，嗯，嗯！大剑将在此打出真蓄\uD83E\uDD17\n" +
                "最后再说一遍\n" +
                "大剑要拔刀了\uD83D\uDE0B\n" +
                "『鬼人粉尘』\n" +
                "『不动衣装』\n" +
                "『勾头拍怒』（Do you like van you see?）\n" +
                "『拔刀斩』\n" +
                "『强化射击』\n" +
                "『嗯～』\n" +
                "『嗯～』\n" +
                "『嗯～』\n" +
                "『shxt』\n" +
                "『Fxck you』\uD83D\uDE24\n" +
                "那么！Made In Heaven！\n" +
                "我以大剑之名命令你退下！\uD83D\uDE21\uD83D\uDE21\uD83D\uDE21"
    ),
    ReplyEntity(
        id = 114514,
        threadId = 1,
        img = "",
        ext = "",
        now = "2022-12-15",
        userHash = "c137reed",
        name = "",
        title = "", "回来吧完美武器\n" +
                "我最骄傲的信仰\n" +
                "历历在目的虫炮\n" +
                "眼泪莫名在流淌\n" +
                "一直记得飞圆斩\n" +
                "还有给你的三灯\n" +
                "把蛇王都给打废\n" +
                "就算通宵也不累"
    ),
    ReplyEntity(
        id = 114514,
        threadId = 1,
        img = "",
        ext = "",
        now = "2022-12-15",
        userHash = "c137reed",
        name = "",
        title = "",
        "刚刚把盾拆完了，原来大家已经聊了这么多了啊"
    ),
    ReplyEntity(
        id = 114514,
        threadId = 1,
        img = "",
        ext = "",
        now = "2022-12-15",
        userHash = "c137reed",
        name = "",
        title = "",
        "最后说一次，「斩斧」要「属性解放」了。\uD83D\uDE21\n" +
                "『属性解放』！\n" +
                "『高速变形』\n" +
                "『金刚连斧』\n" +
                "『属性装填反击』\n" +
                "『零距离属性解放突刺』\n" +
                "『压缩属性解放终结』\n" +
                "『飞翔龙剑』\n" +
                "『铁丝虫步伐』\n" +
                "『变形连斩』\n" +
                "『跳跃属性解放刺』\n" +
                "『二连回旋斩』\n" +
                "『上捞斩』\n" +
                "『突进直斩』\n" +
                "『剑鬼形态』\n" +
                "『暴风之斧』\n" +
                "话已至此，\n" +
                "『属性解放』！"
    ),
    ReplyEntity(
        id = 114514,
        threadId = 1,
        img = "",
        ext = "",
        now = "2022-12-15",
        userHash = "c137reed",
        name = "",
        title = "",
        "本人从未接触过超解，你知道的，我一直都是大回旋的修行者，斩斧一直是我的武器，他强的简直不可思议，我认为我们能产生很好的化学反应。有时候做出决定很难，经过多个日夜的思考，我决定把我的天赋带到斩斧，至于盾斧，祝他拆盾一切顺利。"
    ),
    ReplyEntity(
        id = 114514,
        threadId = 1,
        img = "",
        ext = "",
        now = "2022-12-15",
        userHash = "c137reed",
        name = "",
        title = "",
        "常年玩斩斧的人都目光炯炯有神，非常自信，且智商逐年上升，最后完全成为天才。玩斩斧会改造身体结构，预防各种不治之症。人一旦开始玩斩斧就说明这个人的智慧品行样貌通通都是上等，这辈子都能在任何地方风姿飒爽。斩斧侠具有强烈的社会正义感，对治安稳定有巨大的贡献，保护普通人的生命"
    ),
    ReplyEntity(
        id = 114514,
        threadId = 1,
        img = "",
        ext = "",
        now = "2022-12-15",
        userHash = "c137reed",
        name = "",
        title = "",
        "我，我想起来了\uD83D\uDE26我全都想起来了\uD83D\uDE27我不是大剑侠，不是太刀侠，我，我是，我是斩斧侠！！！斩斧，我真的好喜欢你啊，mua，为了你，我要舍弃真蓄舍弃铁山靠舍弃居合舍弃登龙！我要，我要……我要他x的磁场转动！！！一百万匹力量！！！零距离！属！性！解！放！！！！"
    ),
    ReplyEntity(
        id = 114514,
        threadId = 1,
        img = "",
        ext = "",
        now = "2022-12-15",
        userHash = "c137reed",
        name = "",
        title = "",
        "这个情况还真没见过。这样吧，你先点开mhr，在炎火村或者埃尔加德找到“铁匠铺”点击进入后立即找铁匠大叔或者铁匠姐姐打造新武器，在武器选择页面点击双刀进入双刀打造页面，在双刀打造页面向下拖动，找到爵银龙派生.改，打造装备“刻银灭刃”，在是否装备界面点击“是”，然后走到旁边装备箱子，点击进入，点击“更改替换技”，将迅速切换之书【苍】从上到下设置成“鬼人突进连斩”，“鬼人化”，“回旋斩连段”，“螺旋斩”，“胧翔”，迅速切换之书【朱】“胧翔”替换成“铁虫砥丝”。然后走到旁边随从看板把随从1选个奶猫和随从2选个小偷猫，走到集会所水芸或者据点琪切公主处，点击“怪异调查任务”、“怪异探究任务”，往后翻找到“怪异探究：怪异克服天廻龙 lv300”接取，然后单人或者协同教官火芽出发。"
    ),
    ReplyEntity(
        id = 114514,
        threadId = 1,
        img = "",
        ext = "",
        now = "2022-12-15",
        userHash = "c137reed",
        name = "",
        title = "",
        "这就是我们的双刀\uD83D\uDE00\n" +
                "没问题啊双刀\uD83D\uDE0B\n" +
                "没人能碰到双刀\uD83D\uDE19\n" +
                "我们的双刀还在乱舞\uD83D\uDE0F\n" +
                "卡普空的王牌武器永远是这个能鬼人乱舞的双刀！\uD83D\uDE06\n" +
                "还在乱舞！还在乱舞！\uD83D\uDE03\n" +
                "真·鬼人乱舞！ 我们双刀 ！\uD83E\uDD73\uD83E\uDD73\uD83E\uDD73\n" +
                "保住耐力条！\uD83E\uDD73\uD83E\uDD73\uD83E\uDD73\n" +
                "保住鬼人槽！\uD83E\uDD73\uD83E\uDD73\uD83E\uDD73\n" +
                "辟兽你吃得住双刀的乱舞吗？一个乱舞几十刀\uD83D\uDE0E\n" +
                "鬼人化后在辟兽群里面游龙\uD83D\uDE1C"
    ),
    ReplyEntity(
        id = 114514,
        threadId = 1,
        img = "",
        ext = "",
        now = "2022-12-15",
        userHash = "c137reed",
        name = "",
        title = "",
        "觉得眼熟？\uD83E\uDD14"
    ),
    ReplyEntity(
        id = 114514,
        threadId = 1,
        img = "",
        ext = "",
        now = "2022-12-15",
        userHash = "c137reed",
        name = "",
        title = "",
        "这样的猫车，此时此刻正在这片大陆的各处蔓延！\uD83D\uDE28"
    ),
    ReplyEntity(
        id = 114514,
        threadId = 1,
        img = "",
        ext = "",
        now = "2022-12-15",
        userHash = "c137reed",
        name = "",
        title = "",
        "下一个可能就是你\uD83D\uDC48"
    ),
    ReplyEntity(
        id = 114514,
        threadId = 1,
        img = "",
        ext = "",
        now = "2022-12-15",
        userHash = "c137reed",
        name = "",
        title = "",
        "除非你能做出新大陆上最重要的决定✊"
    )
)
