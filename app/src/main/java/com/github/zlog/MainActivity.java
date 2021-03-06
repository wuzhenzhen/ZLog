package com.github.zlog;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.github.library.ZLog;
import com.github.library.flow.ZFlow;
import com.github.zftpserver.FsService;
import com.github.zftpserver.FsSettings;
import com.github.zftpserver.FtpListener;
import com.github.zftpserver.server.FtpUser;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        //只控制台打印
		ZLog.v("The is verbose log");
//		ZLog.v("The is verbose log", "TAG1", "TAG2", "TAG3");
//		ZLog.d("The is debug log");
//		ZLog.i("The is info log");
//		ZLog.w("The is warn log");
//		ZLog.e("The is error log");
//		ZLog.json("{\"code\":0,\"message\":\"success\",\"result\":[{\"id\":0,\"img\":\"http://i0.hdslb.com/bfs/bangumi/a4e0f26fdd24887749ee023c3d6d2f31eafde946.jpg\",\"is_ad\":0,\"link\":\"http://bangumi.bilibili.com/anime/3461\",\"simg\":\"http://i0.hdslb.com/bfs/bangumi/3e62691b8d95944a355378bf3fdcd1de34549966.jpg\",\"title\":\"完结撒花！\"},{\"id\":0,\"img\":\"http://i0.hdslb.com/bfs/bangumi/43daa400687be05e8b5f2ac43ee2efc431859260.jpg\",\"is_ad\":0,\"link\":\"http://bangumi.bilibili.com/anime/191\",\"simg\":\"http://i0.hdslb.com/bfs/bangumi/95ad9d56dd263668ee90dbfc38428ad7d64cccf4.jpg\",\"title\":\"请问您今天要来点兔子吗？\"},{\"id\":0,\"img\":\"http://i0.hdslb.com/bfs/bangumi/bec36122392fa877cbd786547bc1460ee03b6611.jpg\",\"is_ad\":0,\"link\":\"http://bangumi.bilibili.com/anime/1183\",\"simg\":\"http://i0.hdslb.com/bfs/bangumi/62645a31323f50805101d3ff913a022d61a1a9ac.jpg\",\"title\":\"CODE GEASS 反叛的鲁路修\"},{\"id\":0,\"img\":\"http://i0.hdslb.com/bfs/bangumi/2768afe6997c9dab6455c7efac85d14c9c1ff5da.jpg\",\"is_ad\":0,\"link\":\"http://bangumi.bilibili.com/anime/2546\",\"simg\":\"http://i0.hdslb.com/bfs/bangumi/88a29d2e30288816059865b7bcb07cc6b589d7f2.jpg\",\"title\":\"言叶之庭\"},{\"id\":0,\"img\":\"http://i0.hdslb.com/bfs/bangumi/4b68111b04e9cbd32e4d29c28bfb2bf8b5df8505.jpg\",\"is_ad\":0,\"link\":\"http://bangumi.bilibili.com/anime/3151\",\"simg\":\"http://i0.hdslb.com/bfs/bangumi/1f42b344f5077799f797fdc14c26ec7ff4d1758d.jpg\",\"title\":\"异邦人：无皇刃谭\"}]}");

		//只写入Log 文件
		ZLog.vv("file: The is verbose log");
//		ZLog.dd("file: The is debug log");
//		ZLog.ii("file: The is info log");
//		ZLog.ww("file: The is warn log");
//		ZLog.ee("file: The is error log");
//		ZLog.fjson("{\"code\":0,\"message\":\"success\",\"result\":[{\"id\":0,\"img\":\"http://i0.hdslb.com/bfs/bangumi/a4e0f26fdd24887749ee023c3d6d2f31eafde946.jpg\",\"is_ad\":0,\"link\":\"http://bangumi.bilibili.com/anime/3461\",\"simg\":\"http://i0.hdslb.com/bfs/bangumi/3e62691b8d95944a355378bf3fdcd1de34549966.jpg\",\"title\":\"完结撒花！\"},{\"id\":0,\"img\":\"http://i0.hdslb.com/bfs/bangumi/43daa400687be05e8b5f2ac43ee2efc431859260.jpg\",\"is_ad\":0,\"link\":\"http://bangumi.bilibili.com/anime/191\",\"simg\":\"http://i0.hdslb.com/bfs/bangumi/95ad9d56dd263668ee90dbfc38428ad7d64cccf4.jpg\",\"title\":\"请问您今天要来点兔子吗？\"},{\"id\":0,\"img\":\"http://i0.hdslb.com/bfs/bangumi/bec36122392fa877cbd786547bc1460ee03b6611.jpg\",\"is_ad\":0,\"link\":\"http://bangumi.bilibili.com/anime/1183\",\"simg\":\"http://i0.hdslb.com/bfs/bangumi/62645a31323f50805101d3ff913a022d61a1a9ac.jpg\",\"title\":\"CODE GEASS 反叛的鲁路修\"},{\"id\":0,\"img\":\"http://i0.hdslb.com/bfs/bangumi/2768afe6997c9dab6455c7efac85d14c9c1ff5da.jpg\",\"is_ad\":0,\"link\":\"http://bangumi.bilibili.com/anime/2546\",\"simg\":\"http://i0.hdslb.com/bfs/bangumi/88a29d2e30288816059865b7bcb07cc6b589d7f2.jpg\",\"title\":\"言叶之庭\"},{\"id\":0,\"img\":\"http://i0.hdslb.com/bfs/bangumi/4b68111b04e9cbd32e4d29c28bfb2bf8b5df8505.jpg\",\"is_ad\":0,\"link\":\"http://bangumi.bilibili.com/anime/3151\",\"simg\":\"http://i0.hdslb.com/bfs/bangumi/1f42b344f5077799f797fdc14c26ec7ff4d1758d.jpg\",\"title\":\"异邦人：无皇刃谭\"}]}");
//
		//控制台+写入Log 文件
		ZLog.vvv("All: The is verbose log");
		ZLog.ddd("All: The is debug log");
        ZLog.ddd("All: The is debug log", "TAG1", "TAG2");
//		ZLog.iii("All: The is info log");
//		ZLog.www("All: The is warn log");
		ZLog.eee("All: The is error log");
        ZLog.ddd("{\"list\":[{\"aid\":23740206,\"last_recommend\":[{\"mid\":124416,\"time\":1543063004,\"msg\":\"\",\"uname\":\"ここにいるよ\",\"face\":\"http://i0.hdslb.com/bfs/face/1fd42acf7198403375fbf209e094cf3983182187.jpg\"}],\"typeid\":24,\"typename\":\"MAD·AMV\",\"title\":\"【火影AMV】我的2007 // MY2007\",\"subtitle\":\"\",\"play\":2537,\"review\":29,\"video_review\":12,\"favorites\":218,\"mid\":592002,\"author\":\"非属MAGUS提督后宫分部\",\"description\":\"http://amvnews.ru/index.php?go=Files&in=view&id=9606\\n原作者名: tomik\\n原视频标题: MY2007\\n简介: 燃爆\",\"create\":\"2018-05-21 17:09\",\"pic\":\"http://i2.hdslb.com/bfs/archive/39c0039d7b6da05bd1ad6ca992dc6695de23f7cc.jpg\",\"credit\":0,\"coins\":93,\"duration\":\"3:08\"},{\"aid\":36483768,\"last_recommend\":[{\"mid\":89357,\"time\":1542887019,\"msg\":\"万众期待的高音\",\"uname\":\"时空游客\",\"face\":\"http://i0.hdslb.com/bfs/face/194b37a372d3362e52cf0c620f0f36c3e005cd07.jpg\"}],\"typeid\":22,\"typename\":\"鬼畜调教\",\"title\":\"【高音哥】歌剧2（我的高音，全宇宙最高）\",\"subtitle\":\"\",\"play\":9823,\"review\":58,\"video_review\":114,\"favorites\":214,\"mid\":183410,\"author\":\"XiaoLLK\",\"description\":\"这是一首来自高音哥的宇宙级高音\\n越到后面越高能哦\\nPS：UP主参加了新星计划，希望喜欢这个作品的朋友能多点赞多关注（/TДT)/\\n混音：Gold Rum\\n填词：不鸽木村夫\\n调音、视频：XiaoLLK\",\"create\":\"2018-11-22 18:05\",\"pic\":\"http://i0.hdslb.com/bfs/archive/0ea82cc1bf3d92fe850f3754d183d7343bc4b4e2.jpg\",\"credit\":0,\"coins\":424,\"duration\":\"4:41\"},{\"aid\":36475654,\"last_recommend\":[{\"mid\":29743,\"time\":1542860575,\"msg\":\"\",\"uname\":\"破碎面具\",\"face\":\"http://i0.hdslb.com/bfs/face/1fb9b8842aba749c4ddaf4f1f14fc5602d73cf40.jpg\"}],\"typeid\":25,\"typename\":\"MMD·3D\",\"title\":\"【旗袍改版配布】面具子ROKI（田中姬版）\",\"subtitle\":\"\",\"play\":15570,\"review\":332,\"video_review\":156,\"favorites\":931,\"mid\":29743,\"author\":\"破碎面具\",\"description\":\"借物表：见视频结尾处。\\n\\n前几天说过这个旗袍我又改造了一个低胸版，准备也发一下，于是就发出来了，顺便做个视频演示一下样子。\\nROKI是田中姬的版本~\\n\\n模型配布地址会在视频发布后放在评论里~（毕竟评论里修改起来比视频资料里容易的多）\",\"create\":\"2018-11-22 11:25\",\"pic\":\"http://i1.hdslb.com/bfs/archive/1ed7c5b7509ec382719ef6b5bc6fc2efca82b928.jpg\",\"credit\":0,\"coins\":1486,\"duration\":\"3:57\"},{\"aid\":36076350,\"last_recommend\":[{\"mid\":58426,\"time\":1542616617,\"msg\":\"\",\"uname\":\"残星什么的就是残星\",\"face\":\"http://i0.hdslb.com/bfs/face/56ac36b37662e3746228f30eb4acf2cd332b66a5.jpg\"}],\"typeid\":28,\"typename\":\"原创音乐\",\"title\":\"【KA.U】返校衍生曲《你在哪里》\",\"subtitle\":\"\",\"play\":1832,\"review\":70,\"video_review\":85,\"favorites\":172,\"mid\":18977573,\"author\":\"KAU中文配音社团\",\"description\":\"Sc\\n原著：赤烛游戏《返校》\\n策划/剧本/PV构思/海报构思：西塔【KA.U】\\n编曲/分轨混音：爆豪胜姬\\n作曲：爆豪胜姬，张卫帆（见注释1）\\n作词：不如卿\\n演唱：费囧【KA.U】\\n歌曲人声后期：小仙女后期工作室\\n剧情后期：故安【KA.U】\\nPV：昭乱【初C工作室】\\nPV素材鸣谢：半支烟sama（见注释2）\\n海报：波斯菊【KA.U】\\n\\nCV：\\n方芮欣：纸巾【KA.U】\\n张明辉：江笙【翼之声】\\n殷翠涵：青钺森森【KA.U】\\n魏仲庭：清觞【KA.U】\\n电话男声：浩然【KA.U】\\n\\n\\n方芮欣【独白】：\\n（独自一人\",\"create\":\"2018-11-17 19:00\",\"pic\":\"http://i1.hdslb.com/bfs/archive/1fbe79363dc796542993b1a83b00b4b2fafd39e9.jpg\",\"credit\":0,\"coins\":252,\"duration\":\"5:29\"},{\"aid\":35679755,\"last_recommend\":[{\"mid\":294646,\"time\":1541853272,\"msg\":\"\",\"uname\":\"毛酱·把名字还给我\",\"face\":\"http://i0.hdslb.com/bfs/face/b2345c23456f93d865cdaac3905a6157f1f4ea06.jpg\"}],\"typeid\":31,\"typename\":\"翻唱\",\"title\":\"【翻唱】スキキライ ft. Orion【A. Ikari】\",\"subtitle\":\"\",\"play\":1465,\"review\":44,\"video_review\":4,\"favorites\":50,\"mid\":436256,\"author\":\"A_Ikari\",\"description\":\"可是又不是真的14岁（摊手\\n\\n和往年一样为了不影响大家剁手提前一天发_(:з」∠)_\\n祝大家明天剁手开心（手动doge\\n不说了我去整理购物车了_(:з」∠)_\\n\\n前作：光彼方 → av27346906\",\"create\":\"2018-11-10 16:32\",\"pic\":\"http://i1.hdslb.com/bfs/archive/ea78412a091fa423a3bf5857f7ea90bb30ebcbce.jpg\",\"credit\":0,\"coins\":161,\"duration\":\"4:59\"},{\"aid\":34942885,\"last_recommend\":[{\"mid\":2560,\"time\":1541756023,\"msg\":\"\",\"uname\":\"-林檎消失-\",\"face\":\"http://i1.hdslb.com/bfs/face/3474a5d9ba4c29629cfa725f581a9e6a1c4188b7.jpg\"}],\"typeid\":20,\"typename\":\"宅舞\",\"title\":\"【蓝鹅】✟Happy Halloween✟万圣节快乐~\",\"subtitle\":\"\",\"play\":9853,\"review\":62,\"video_review\":124,\"favorites\":380,\"mid\":89556,\"author\":\"laner\",\"description\":\"大家万圣节快乐~不给糖就捣蛋啦！这次参加了活动，大家喜欢的话麻烦点赞投币收藏呀，APP升级最新版本可以一键一条龙~\\n\\n原创振幅：sm24733261\\n使用音源：sm24734630\\n摄影：兔子命/后期：七分\\n后勤：兔子、小璇\\n\\n↓下面是一些碎碎念\\n第一次尝试夜景，结果不是很好，感谢后期拯救我的死亡黄光！下次录夜景会准备的更加充分的！这个舞也是历经波折，第一次出了意外，这是录的第二次，也是困难重重，不过好在顺利产出啦~第二作没有很大的进步，表情和动作都没能做到更好，以后会更加努力练习。\\n感谢摄影和后勤，冷天\",\"create\":\"2018-10-30 19:17\",\"pic\":\"http://i2.hdslb.com/bfs/archive/4ec30eda41b37633ad512d90041a5d1ab73e1269.jpg\",\"credit\":0,\"coins\":319,\"duration\":\"4:18\"},{\"aid\":23740206,\"last_recommend\":[{\"mid\":124416,\"time\":1543063004,\"msg\":\"\",\"uname\":\"ここにいるよ\",\"face\":\"http://i0.hdslb.com/bfs/face/1fd42acf7198403375fbf209e094cf3983182187.jpg\"}],\"typeid\":24,\"typename\":\"MAD·AMV\",\"title\":\"【火影AMV】我的2007 // MY2007\",\"subtitle\":\"\",\"play\":2537,\"review\":29,\"video_review\":12,\"favorites\":218,\"mid\":592002,\"author\":\"非属MAGUS提督后宫分部\",\"description\":\"http://amvnews.ru/index.php?go=Files&in=view&id=9606\\n原作者名: tomik\\n原视频标题: MY2007\\n简介: 燃爆\",\"create\":\"2018-05-21 17:09\",\"pic\":\"http://i2.hdslb.com/bfs/archive/39c0039d7b6da05bd1ad6ca992dc6695de23f7cc.jpg\",\"credit\":0,\"coins\":93,\"duration\":\"3:08\"},{\"aid\":23740206,\"last_recommend\":[{\"mid\":124416,\"time\":1543063004,\"msg\":\"\",\"uname\":\"ここにいるよ\",\"face\":\"http://i0.hdslb.com/bfs/face/1fd42acf7198403375fbf209e094cf3983182187.jpg\"}],\"typeid\":24,\"typename\":\"MAD·AMV\",\"title\":\"【火影AMV】我的2007 // MY2007\",\"subtitle\":\"\",\"play\":2537,\"review\":29,\"video_review\":12,\"favorites\":218,\"mid\":592002,\"author\":\"非属MAGUS提督后宫分部\",\"description\":\"http://amvnews.ru/index.php?go=Files&in=view&id=9606\\n原作者名: tomik\\n原视频标题: MY2007\\n简介: 燃爆\",\"create\":\"2018-05-21 17:09\",\"pic\":\"http://i2.hdslb.com/bfs/archive/39c0039d7b6da05bd1ad6ca992dc6695de23f7cc.jpg\",\"credit\":0,\"coins\":93,\"duration\":\"3:08\"},{\"aid\":23740206,\"last_recommend\":[{\"mid\":124416,\"time\":1543063004,\"msg\":\"\",\"uname\":\"ここにいるよ\",\"face\":\"http://i0.hdslb.com/bfs/face/1fd42acf7198403375fbf209e094cf3983182187.jpg\"}],\"typeid\":24,\"typename\":\"MAD·AMV\",\"title\":\"【火影AMV】我的2007 // MY2007\",\"subtitle\":\"\",\"play\":2537,\"review\":29,\"video_review\":12,\"favorites\":218,\"mid\":592002,\"author\":\"非属MAGUS提督后宫分部\",\"description\":\"http://amvnews.ru/index.php?go=Files&in=view&id=9606\\n原作者名: tomik\\n原视频标题: MY2007\\n简介: 燃爆\",\"create\":\"2018-05-21 17:09\",\"pic\":\"http://i2.hdslb.com/bfs/archive/39c0039d7b6da05bd1ad6ca992dc6695de23f7cc.jpg\",\"credit\":0,\"coins\":93,\"duration\":\"3:08\"},{\"aid\":23740206,\"last_recommend\":[{\"mid\":124416,\"time\":1543063004,\"msg\":\"\",\"uname\":\"ここにいるよ\",\"face\":\"http://i0.hdslb.com/bfs/face/1fd42acf7198403375fbf209e094cf3983182187.jpg\"}],\"typeid\":24,\"typename\":\"MAD·AMV\",\"title\":\"【火影AMV】我的2007 // MY2007\",\"subtitle\":\"\",\"play\":2537,\"review\":29,\"video_review\":12,\"favorites\":218,\"mid\":592002,\"author\":\"非属MAGUS提督后宫分部\",\"description\":\"http://amvnews.ru/index.php?go=Files&in=view&id=9606\\n原作者名: tomik\\n原视频标题: MY2007\\n简介: 燃爆\",\"create\":\"2018-05-21 17:09\",\"pic\":\"http://i2.hdslb.com/bfs/archive/39c0039d7b6da05bd1ad6ca992dc6695de23f7cc.jpg\",\"credit\":0,\"coins\":93,\"duration\":\"3:08\"},{\"aid\":23740206,\"last_recommend\":[{\"mid\":124416,\"time\":1543063004,\"msg\":\"\",\"uname\":\"ここにいるよ\",\"face\":\"http://i0.hdslb.com/bfs/face/1fd42acf7198403375fbf209e094cf3983182187.jpg\"}],\"typeid\":24,\"typename\":\"MAD·AMV\",\"title\":\"【火影AMV】我的2007 // MY2007\",\"subtitle\":\"\",\"play\":2537,\"review\":29,\"video_review\":12,\"favorites\":218,\"mid\":592002,\"author\":\"非属MAGUS提督后宫分部\",\"description\":\"http://amvnews.ru/index.php?go=Files&in=view&id=9606\\n原作者名: tomik\\n原视频标题: MY2007\\n简介: 燃爆\",\"create\":\"2018-05-21 17:09\",\"pic\":\"http://i2.hdslb.com/bfs/archive/39c0039d7b6da05bd1ad6ca992dc6695de23f7cc.jpg\",\"credit\":0,\"coins\":93,\"duration\":\"3:08\"},{\"aid\":23740206,\"last_recommend\":[{\"mid\":124416,\"time\":1543063004,\"msg\":\"\",\"uname\":\"ここにいるよ\",\"face\":\"http://i0.hdslb.com/bfs/face/1fd42acf7198403375fbf209e094cf3983182187.jpg\"}],\"typeid\":24,\"typename\":\"MAD·AMV\",\"title\":\"【火影AMV】我的2007 // MY2007\",\"subtitle\":\"\",\"play\":2537,\"review\":29,\"video_review\":12,\"favorites\":218,\"mid\":592002,\"author\":\"非属MAGUS提督后宫分部\",\"description\":\"http://amvnews.ru/index.php?go=Files&in=view&id=9606\\n原作者名: tomik\\n原视频标题: MY2007\\n简介: 燃爆\",\"create\":\"2018-05-21 17:09\",\"pic\":\"http://i2.hdslb.com/bfs/archive/39c0039d7b6da05bd1ad6ca992dc6695de23f7cc.jpg\",\"credit\":0,\"coins\":93,\"duration\":\"3:08\"},{\"aid\":23740206,\"last_recommend\":[{\"mid\":124416,\"time\":1543063004,\"msg\":\"\",\"uname\":\"ここにいるよ\",\"face\":\"http://i0.hdslb.com/bfs/face/1fd42acf7198403375fbf209e094cf3983182187.jpg\"}],\"typeid\":24,\"typename\":\"MAD·AMV\",\"title\":\"【火影AMV】我的2007 // MY2007\",\"subtitle\":\"\",\"play\":2537,\"review\":29,\"video_review\":12,\"favorites\":218,\"mid\":592002,\"author\":\"非属MAGUS提督后宫分部\",\"description\":\"http://amvnews.ru/index.php?go=Files&in=view&id=9606\\n原作者名: tomik\\n原视频标题: MY2007\\n简介: 燃爆\",\"create\":\"2018-05-21 17:09\",\"pic\":\"http://i2.hdslb.com/bfs/archive/39c0039d7b6da05bd1ad6ca992dc6695de23f7cc.jpg\",\"credit\":0,\"coins\":93,\"duration\":\"3:08\"},{\"aid\":23740206,\"last_recommend\":[{\"mid\":124416,\"time\":1543063004,\"msg\":\"\",\"uname\":\"ここにいるよ\",\"face\":\"http://i0.hdslb.com/bfs/face/1fd42acf7198403375fbf209e094cf3983182187.jpg\"}],\"typeid\":24,\"typename\":\"MAD·AMV\",\"title\":\"【火影AMV】我的2007 // MY2007\",\"subtitle\":\"\",\"play\":2537,\"review\":29,\"video_review\":12,\"favorites\":218,\"mid\":592002,\"author\":\"非属MAGUS提督后宫分部\",\"description\":\"http://amvnews.ru/index.php?go=Files&in=view&id=9606\\n原作者名: tomik\\n原视频标题: MY2007\\n简介: 燃爆\",\"create\":\"2018-05-21 17:09\",\"pic\":\"http://i2.hdslb.com/bfs/archive/39c0039d7b6da05bd1ad6ca992dc6695de23f7cc.jpg\",\"credit\":0,\"coins\":93,\"duration\":\"3:08\"},{\"aid\":23740206,\"last_recommend\":[{\"mid\":124416,\"time\":1543063004,\"msg\":\"\",\"uname\":\"ここにいるよ\",\"face\":\"http://i0.hdslb.com/bfs/face/1fd42acf7198403375fbf209e094cf3983182187.jpg\"}],\"typeid\":24,\"typename\":\"MAD·AMV\",\"title\":\"【火影AMV】我的2007 // MY2007\",\"subtitle\":\"\",\"play\":2537,\"review\":29,\"video_review\":12,\"favorites\":218,\"mid\":592002,\"author\":\"非属MAGUS提督后宫分部\",\"description\":\"http://amvnews.ru/index.php?go=Files&in=view&id=9606\\n原作者名: tomik\\n原视频标题: MY2007\\n简介: 燃爆\",\"create\":\"2018-05-21 17:09\",\"pic\":\"http://i2.hdslb.com/bfs/archive/39c0039d7b6da05bd1ad6ca992dc6695de23f7cc.jpg\",\"credit\":0,\"coins\":93,\"duration\":\"3:08\"},{\"aid\":23740206,\"last_recommend\":[{\"mid\":124416,\"time\":1543063004,\"msg\":\"\",\"uname\":\"ここにいるよ\",\"face\":\"http://i0.hdslb.com/bfs/face/1fd42acf7198403375fbf209e094cf3983182187.jpg\"}],\"typeid\":24,\"typename\":\"MAD·AMV\",\"title\":\"【火影AMV】我的2007 // MY2007\",\"subtitle\":\"\",\"play\":2537,\"review\":29,\"video_review\":12,\"favorites\":218,\"mid\":592002,\"author\":\"非属MAGUS提督后宫分部\",\"description\":\"http://amvnews.ru/index.php?go=Files&in=view&id=9606\\n原作者名: tomik\\n原视频标题: MY2007\\n简介: 燃爆\",\"create\":\"2018-05-21 17:09\",\"pic\":\"http://i2.hdslb.com/bfs/archive/39c0039d7b6da05bd1ad6ca992dc6695de23f7cc.jpg\",\"credit\":0,\"coins\":93,\"duration\":\"3:08\"},{\"aid\":23740206,\"last_recommend\":[{\"mid\":124416,\"time\":1543063004,\"msg\":\"\",\"uname\":\"ここにいるよ\",\"face\":\"http://i0.hdslb.com/bfs/face/1fd42acf7198403375fbf209e094cf3983182187.jpg\"}],\"typeid\":24,\"typename\":\"MAD·AMV\",\"title\":\"【火影AMV】我的2007 // MY2007\",\"subtitle\":\"\",\"play\":2537,\"review\":29,\"video_review\":12,\"favorites\":218,\"mid\":592002,\"author\":\"非属MAGUS提督后宫分部\",\"description\":\"http://amvnews.ru/index.php?go=Files&in=view&id=9606\\n原作者名: tomik\\n原视频标题: MY2007\\n简介: 燃爆\",\"create\":\"2018-05-21 17:09\",\"pic\":\"http://i2.hdslb.com/bfs/archive/39c0039d7b6da05bd1ad6ca992dc6695de23f7cc.jpg\",\"credit\":0,\"coins\":93,\"duration\":\"3:08\"},{\"aid\":23740206,\"last_recommend\":[{\"mid\":124416,\"time\":1543063004,\"msg\":\"\",\"uname\":\"ここにいるよ\",\"face\":\"http://i0.hdslb.com/bfs/face/1fd42acf7198403375fbf209e094cf3983182187.jpg\"}],\"typeid\":24,\"typename\":\"MAD·AMV\",\"title\":\"【火影AMV】我的2007 // MY2007\",\"subtitle\":\"\",\"play\":2537,\"review\":29,\"video_review\":12,\"favorites\":218,\"mid\":592002,\"author\":\"非属MAGUS提督后宫分部\",\"description\":\"http://amvnews.ru/index.php?go=Files&in=view&id=9606\\n原作者名: tomik\\n原视频标题: MY2007\\n简介: 燃爆\",\"create\":\"2018-05-21 17:09\",\"pic\":\"http://i2.hdslb.com/bfs/archive/39c0039d7b6da05bd1ad6ca992dc6695de23f7cc.jpg\",\"credit\":0,\"coins\":93,\"duration\":\"3:08\"},{\"aid\":23740206,\"last_recommend\":[{\"mid\":124416,\"time\":1543063004,\"msg\":\"\",\"uname\":\"ここにいるよ\",\"face\":\"http://i0.hdslb.com/bfs/face/1fd42acf7198403375fbf209e094cf3983182187.jpg\"}],\"typeid\":24,\"typename\":\"MAD·AMV\",\"title\":\"【火影AMV】我的2007 // MY2007\",\"subtitle\":\"\",\"play\":2537,\"review\":29,\"video_review\":12,\"favorites\":218,\"mid\":592002,\"author\":\"非属MAGUS提督后宫分部\",\"description\":\"http://amvnews.ru/index.php?go=Files&in=view&id=9606\\n原作者名: tomik\\n原视频标题: MY2007\\n简介: 燃爆\",\"create\":\"2018-05-21 17:09\",\"pic\":\"http://i2.hdslb.com/bfs/archive/39c0039d7b6da05bd1ad6ca992dc6695de23f7cc.jpg\",\"credit\":0,\"coins\":93,\"duration\":\"3:08\"}]}");
		ZLog.cfjson("{\"code\":0,\"message\":\"success\",\"result\":[{\"id\":0,\"img\":\"http://i0.hdslb.com/bfs/bangumi/a4e0f26fdd24887749ee023c3d6d2f31eafde946.jpg\",\"is_ad\":0,\"link\":\"http://bangumi.bilibili.com/anime/3461\",\"simg\":\"http://i0.hdslb.com/bfs/bangumi/3e62691b8d95944a355378bf3fdcd1de34549966.jpg\",\"title\":\"完结撒花！\"},{\"id\":0,\"img\":\"http://i0.hdslb.com/bfs/bangumi/43daa400687be05e8b5f2ac43ee2efc431859260.jpg\",\"is_ad\":0,\"link\":\"http://bangumi.bilibili.com/anime/191\",\"simg\":\"http://i0.hdslb.com/bfs/bangumi/95ad9d56dd263668ee90dbfc38428ad7d64cccf4.jpg\",\"title\":\"请问您今天要来点兔子吗？\"},{\"id\":0,\"img\":\"http://i0.hdslb.com/bfs/bangumi/bec36122392fa877cbd786547bc1460ee03b6611.jpg\",\"is_ad\":0,\"link\":\"http://bangumi.bilibili.com/anime/1183\",\"simg\":\"http://i0.hdslb.com/bfs/bangumi/62645a31323f50805101d3ff913a022d61a1a9ac.jpg\",\"title\":\"CODE GEASS 反叛的鲁路修\"},{\"id\":0,\"img\":\"http://i0.hdslb.com/bfs/bangumi/2768afe6997c9dab6455c7efac85d14c9c1ff5da.jpg\",\"is_ad\":0,\"link\":\"http://bangumi.bilibili.com/anime/2546\",\"simg\":\"http://i0.hdslb.com/bfs/bangumi/88a29d2e30288816059865b7bcb07cc6b589d7f2.jpg\",\"title\":\"言叶之庭\"},{\"id\":0,\"img\":\"http://i0.hdslb.com/bfs/bangumi/4b68111b04e9cbd32e4d29c28bfb2bf8b5df8505.jpg\",\"is_ad\":0,\"link\":\"http://bangumi.bilibili.com/anime/3151\",\"simg\":\"http://i0.hdslb.com/bfs/bangumi/1f42b344f5077799f797fdc14c26ec7ff4d1758d.jpg\",\"title\":\"异邦人：无皇刃谭\"}]}");

//		//写入Log 文件,且指定 Log文件名
//		ZLog.fv("Hello World!", "MyLog.txt");
//
//		//控制台+写入Log 文件,且指定 Log文件名
//		ZLog.fvv("Hello World!!", "MyLog.txt");

//        ZLog.ddd("output file msg");
            //灵活的链式调用api
//        ZLog.v().msg("output msg").out();
//        ZLog.v().msg("output msg").msg("result = ").msg(2 + 2).out();
//        ZLog.v().msgs("output msgs is ", 1, 2, 3).out();
//
//        ZLog.v().tag("newTag").msg("output msg").out();
//        ZLog.v().msg("output msg").tag("newTag").msg("result = ").msg(2 + 2).out();
//        ZLog.v().msgs("output msgs is ", 1, 2, 3).tag("newTag").out();

        //
//        ZLog.v().file().msg("output file msg").out();
//        ZLog.v().file().msg("output file msg").msg("result = ").msg(2 + 2).out();
//        ZLog.v().file().msgs("output file msgs is ", 1, 2, 3).out();

//        ZLog.v().file().tag("newTag").msg("output file msg").out();
//        ZLog.v().file().msg("output file msg").tag("newTag").msg("result = ").msg(2 + 2).out();
//        ZLog.v().file().msgs("output file msgs is ", 1, 2, 3).tag("newTag").file("newFileName").msg("当然你也可指定文件名 ").out();
//
//
//        ZLog.e().msg("output error msg").out().ln().msg("Do you see? 。。。。").format("a:%s;b:%s", 2, 3).out();
        //... Test More

        ZLog.eee("=========Flow==========start");
        ZLog.iii("---flow="+ZFlow.getSumFlowYesterday());  //打印昨天的流量数据
        ZLog.iii("---flow="+ZFlow.getSumFlow(System.currentTimeMillis())); //打印某一天的流量数据
        ZLog.eee("=========Flow==========end");
    }

    public void openFtp(View view){
        ZLog.eee("====ftp=====openFtp==========");
        if(FsSettings.getUser("ftp2") != null){
            FsSettings.addUser(new FtpUser("ftp2","ftp2","\\")); //添加FTP用户名
        }else{
            ZLog.eee("====ftp=====openFtp=======already=exist=user===");
        }
        FsService.start(this.getApplicationContext(), new FtpListener() {
            @Override
            public void openFtp(int type, String message) {
                ZLog.eee("==ftp==openFtp="+type+"--"+message);
            }

            @Override
            public void closeFtp() {
                ZLog.eee("==ftp==closeFtp=");
            }

            @Override
            public void login(int type, String message) {
                ZLog.eee("==ftp==login="+type+"--"+message);
            }

            @Override
            public void addFile(String file) {
                ZLog.eee("==ftp==addFile="+file);
            }

            @Override
            public void delFile(String file) {
                ZLog.eee("==ftp==delFile="+file);
            }

            @Override
            public void log(String log) {
                ZLog.ddd("##log##"+log);
            }
        });

    }

    public void closeFtp(View view){
        ZLog.eee("====ftp=====closeFtp==========");
        FsService.stop();
    }
}
