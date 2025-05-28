/*
 Navicat Premium Dump SQL

 Source Server         : MyDB
 Source Server Type    : MySQL
 Source Server Version : 90100 (9.1.0)
 Source Host           : localhost:3306
 Source Schema         : gforo_old_version

 Target Server Type    : MySQL
 Target Server Version : 90100 (9.1.0)
 File Encoding         : 65001

 Date: 27/05/2025 23:45:47
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for board
-- ----------------------------
DROP TABLE IF EXISTS `board`;
CREATE TABLE `board` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `name` varchar(50) NOT NULL COMMENT '板块名称',
  `description` varchar(255) DEFAULT NULL COMMENT '板块介绍',
  `create_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `status` int DEFAULT '0' COMMENT '0-正常 1-隐藏 2-封禁',
  `icon_url` varchar(255) DEFAULT NULL COMMENT '板块图标URL',
  PRIMARY KEY (`id`),
  UNIQUE KEY `name` (`name`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- ----------------------------
-- Records of board
-- ----------------------------
BEGIN;
INSERT INTO `board` (`id`, `name`, `description`, `create_time`, `status`, `icon_url`) VALUES (1, 'Technical', 'Discuss technology-related issues and knowledge sharing', '2025-05-02 17:28:08', 1, 'https://pettownsendvet.com/wp-content/uploads/2023/01/iStock-1052880600.jpg');
INSERT INTO `board` (`id`, `name`, `description`, `create_time`, `status`, `icon_url`) VALUES (2, 'Life Sharing', 'Share anecdotes and insights from your life', '2025-05-02 17:49:15', 1, 'https://pettownsendvet.com/wp-content/uploads/2023/01/iStock-1052880600.jpg');
INSERT INTO `board` (`id`, `name`, `description`, `create_time`, `status`, `icon_url`) VALUES (3, 'Daily Chat', 'Relaxed communication space', '2025-05-02 17:49:15', 1, 'https://pettownsendvet.com/wp-content/uploads/2023/01/iStock-1052880600.jpg');
COMMIT;

-- ----------------------------
-- Table structure for board_poster
-- ----------------------------
DROP TABLE IF EXISTS `board_poster`;
CREATE TABLE `board_poster` (
  `user_id` bigint NOT NULL,
  `board_id` bigint NOT NULL,
  PRIMARY KEY (`user_id`,`board_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- ----------------------------
-- Records of board_poster
-- ----------------------------
BEGIN;
INSERT INTO `board_poster` (`user_id`, `board_id`) VALUES (1, 2);
INSERT INTO `board_poster` (`user_id`, `board_id`) VALUES (2, 1);
INSERT INTO `board_poster` (`user_id`, `board_id`) VALUES (2, 2);
INSERT INTO `board_poster` (`user_id`, `board_id`) VALUES (2, 3);
INSERT INTO `board_poster` (`user_id`, `board_id`) VALUES (5, 3);
INSERT INTO `board_poster` (`user_id`, `board_id`) VALUES (8, 3);
INSERT INTO `board_poster` (`user_id`, `board_id`) VALUES (9, 2);
COMMIT;

-- ----------------------------
-- Table structure for carousel
-- ----------------------------
DROP TABLE IF EXISTS `carousel`;
CREATE TABLE `carousel` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `image_url` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `title` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `description` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `target_url` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ----------------------------
-- Records of carousel
-- ----------------------------
BEGIN;
INSERT INTO `carousel` (`id`, `image_url`, `title`, `description`, `target_url`, `created_at`, `updated_at`) VALUES (7, 'https://i.pinimg.com/736x/09/33/34/0933344aeac414dffeeacf2719572dff.jpg', 'Hi Everyone', 'You can discuss anything here.', 'https://www.youtube.com/watch?v=Y-OjP_acn64&list=PL7dyzQE15o6acIR2yDbDFM6-Kp1vfTrrR', NULL, NULL);
INSERT INTO `carousel` (`id`, `image_url`, `title`, `description`, `target_url`, `created_at`, `updated_at`) VALUES (8, 'https://jazitime.com/wp-content/uploads/2024/01/guimie_500.png', 'Become a Moderator', 'Moderators will be assigned by the administrator based on contributions', '', NULL, NULL);
INSERT INTO `carousel` (`id`, `image_url`, `title`, `description`, `target_url`, `created_at`, `updated_at`) VALUES (9, 'https://img-s.msn.cn/tenant/amp/entityid/BB1oz4Pt.img?w=768&h=432&m=6&x=501&y=226&s=237&d=237', 'Testing phase', 'Likes may be cleared during the testing phase', 'https://www.youtube.com/watch?v=meikcMCkRig', NULL, NULL);
COMMIT;

-- ----------------------------
-- Table structure for comment
-- ----------------------------
DROP TABLE IF EXISTS `comment`;
CREATE TABLE `comment` (
  `id` int NOT NULL AUTO_INCREMENT,
  `user_id` int DEFAULT NULL,
  `entity_type` int DEFAULT NULL,
  `entity_id` int DEFAULT NULL,
  `target_id` int DEFAULT NULL,
  `content` mediumtext CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci,
  `status` int DEFAULT NULL,
  `create_time` timestamp NULL DEFAULT NULL,
  `post_id` int DEFAULT '0' COMMENT '父评论ID，0表示一级评论',
  PRIMARY KEY (`id`),
  KEY `index_user_id` (`user_id`),
  KEY `index_entity_id` (`entity_id`)
) ENGINE=InnoDB AUTO_INCREMENT=294 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- ----------------------------
-- Records of comment
-- ----------------------------
BEGIN;
INSERT INTO `comment` (`id`, `user_id`, `entity_type`, `entity_id`, `target_id`, `content`, `status`, `create_time`, `post_id`) VALUES (268, 19, 0, 67, 0, 'reply test', 1, '2025-05-23 11:29:54', 67);
INSERT INTO `comment` (`id`, `user_id`, `entity_type`, `entity_id`, `target_id`, `content`, `status`, `create_time`, `post_id`) VALUES (269, 19, 1, 67, 0, 'reply test2', 1, '2025-05-23 11:30:00', 67);
INSERT INTO `comment` (`id`, `user_id`, `entity_type`, `entity_id`, `target_id`, `content`, `status`, `create_time`, `post_id`) VALUES (270, 19, 2, 268, 0, 'reply test3', 1, '2025-05-23 11:30:14', 67);
INSERT INTO `comment` (`id`, `user_id`, `entity_type`, `entity_id`, `target_id`, `content`, `status`, `create_time`, `post_id`) VALUES (271, 19, 1, 68, 0, 'hiiiiii ', 1, '2025-05-23 13:15:13', 68);
INSERT INTO `comment` (`id`, `user_id`, `entity_type`, `entity_id`, `target_id`, `content`, `status`, `create_time`, `post_id`) VALUES (272, 19, 0, 68, 0, 'this is a reloj to post', 1, '2025-05-23 13:15:38', 68);
INSERT INTO `comment` (`id`, `user_id`, `entity_type`, `entity_id`, `target_id`, `content`, `status`, `create_time`, `post_id`) VALUES (273, 19, 0, 69, 0, 'Thanks for your feedback!\n\n- **Why this site?** A personal project to enrich my resume. I learned Java and middleware, and practiced system design through this.\n- **Testing phase?** 1–2 months while I finish remaining features.\n- **Next steps?** Will stay live after launch. I’ll open source both frontend and backend (backend supports Docker deploy).  \nIf you\'re curious, email me—I can share the backend code.', 1, '2025-05-23 14:04:38', 69);
INSERT INTO `comment` (`id`, `user_id`, `entity_type`, `entity_id`, `target_id`, `content`, `status`, `create_time`, `post_id`) VALUES (274, 19, 0, 69, 0, 'Just realized replies have a tight character limit—unlike the main post. That explains why mine got cut short. Also noticed the UI doesn\'t give clear hints about this. I\'ll work on improving that soon. Thanks again for pointing things out!![Description](https://naeye.net/wp-content/uploads/2019/11/this-cube-900x600.png)', 1, '2025-05-23 14:09:02', 69);
INSERT INTO `comment` (`id`, `user_id`, `entity_type`, `entity_id`, `target_id`, `content`, `status`, `create_time`, `post_id`) VALUES (275, 19, 1, 69, 0, 'If you wanna see those functions about admin or board manager , test me. I will give you permission to your count. So that in the menu you can see “gestión” module', 1, '2025-05-23 14:14:17', 69);
INSERT INTO `comment` (`id`, `user_id`, `entity_type`, `entity_id`, `target_id`, `content`, `status`, `create_time`, `post_id`) VALUES (276, 19, 1, 69, 0, 'If you wanna see those functions about admin or board manager , test me. I will give you permission to your count. So that in the menu you can see “gestión” module', 0, '2025-05-23 14:14:17', 69);
INSERT INTO `comment` (`id`, `user_id`, `entity_type`, `entity_id`, `target_id`, `content`, `status`, `create_time`, `post_id`) VALUES (277, 19, 1, 69, 0, 'As you can see. Now it does not have “add new board” part. I am trying do it interestly like throw vote o has limit of exp or count of post or like , something like that. I am still considering how to do it. And it had the part of live chat part in the front. But emm considering how to do it too', 1, '2025-05-23 14:19:49', 69);
INSERT INTO `comment` (`id`, `user_id`, `entity_type`, `entity_id`, `target_id`, `content`, `status`, `create_time`, `post_id`) VALUES (278, 19, 1, 69, 0, 'The most difficult part is the design of table of database 🥲', 1, '2025-05-23 14:20:40', 69);
INSERT INTO `comment` (`id`, `user_id`, `entity_type`, `entity_id`, `target_id`, `content`, `status`, `create_time`, `post_id`) VALUES (279, 24, 1, 69, 0, ' I have to say it\'s kinda odd that there are 2 different types of responses.', 0, '2025-05-23 14:27:06', 69);
INSERT INTO `comment` (`id`, `user_id`, `entity_type`, `entity_id`, `target_id`, `content`, `status`, `create_time`, `post_id`) VALUES (280, 24, 1, 69, 19, 'Aaah yeah databases can be quite odd sometimes. i\'d love to help with further testing. I don\'t know how active I\'ll be with work and other stuff but i\'d be happy to help!', 1, '2025-05-23 14:30:42', 69);
INSERT INTO `comment` (`id`, `user_id`, `entity_type`, `entity_id`, `target_id`, `content`, `status`, `create_time`, `post_id`) VALUES (281, 19, 1, 69, 24, 'Hi , I just give you permission of admin. Now you can see your menu. It will show the part of gestión. Thanks very much for your feedback and your time !!!!', 1, '2025-05-23 14:34:17', 69);
INSERT INTO `comment` (`id`, `user_id`, `entity_type`, `entity_id`, `target_id`, `content`, `status`, `create_time`, `post_id`) VALUES (282, 19, 1, 69, 24, 'Admin has all right for users with lower level. And it can grante role or title to others. To set title , you can do it in profile page', 1, '2025-05-23 14:40:29', 69);
INSERT INTO `comment` (`id`, `user_id`, `entity_type`, `entity_id`, `target_id`, `content`, `status`, `create_time`, `post_id`) VALUES (283, 19, 0, 70, 0, '## 📤 Upload File Module\n\nBecause I\'m currently broke 💸, I can\'t afford to support native file uploads yet.  \nSo for now, **please upload your file to your own cloud drive or GitHub and just share the link**.  \n\n> ![](https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQ_lYdqVblVdeWga9PqSVKnRXPFkPfY2y-bjQ&s)  \n> *I\'m doing my best on a tight budget, okay?*\n\n---\n\n## 🛠 Section Creation Module\n\nI\'m still working on this part.  \nInstead of simply giving section creation rights to admins, I want to make it more **fun and engaging**.\n\nMy goal:  \n- Make it feel like you\'re **giving birth to a new idea** 🐣  \n- Maybe even involve the community — voting, suggestions, etc.  \n- Let section creation feel special, not just \"admin power\"\n\n> ![](https://p5.itc.cn/q_70/images03/20210219/4a1bbb56adc548de9525178250193a1a.gif)  \n> *Me trying to design the perfect section creation experience.*\n\n---\n\n## 📬 Private Message Module (Maybe do this as a live-chat)\n\nThis one might take a while, because I want to do it **properly**:\n\n1. Design a solid message table structure in the database 📚  \n2. Implement **unread message reminders** 🔔  \n3. Add control over **who can send messages** — I don’t want spam flooding the system\n\nWhat I’m planning:  \n- Only allow DMs from mutual followers (Maybe)  \n- Add block/report functionality  \n- Notifications for new and unread messages  \n\n> ![](https://www.ntet.cn/uploads/images/20220217/20220217133401_96452.png)  \n> *If I don’t put restrictions on messaging, this will be everyone’s inbox.*\n\n---\n\nThanks for your patience 💖 I promise I’m building it with love and caffeine ☕\n', 1, '2025-05-25 01:16:24', 70);
INSERT INTO `comment` (`id`, `user_id`, `entity_type`, `entity_id`, `target_id`, `content`, `status`, `create_time`, `post_id`) VALUES (284, 19, 1, 70, 0, 'test', 0, '2025-05-25 01:24:20', 70);
INSERT INTO `comment` (`id`, `user_id`, `entity_type`, `entity_id`, `target_id`, `content`, `status`, `create_time`, `post_id`) VALUES (285, 23, 1, 70, 0, 'sdsf', 0, '2025-05-25 01:25:06', 70);
INSERT INTO `comment` (`id`, `user_id`, `entity_type`, `entity_id`, `target_id`, `content`, `status`, `create_time`, `post_id`) VALUES (286, 19, 1, 73, 0, 'test', 0, '2025-05-26 02:07:00', 73);
COMMIT;

-- ----------------------------
-- Table structure for discuss_post
-- ----------------------------
DROP TABLE IF EXISTS `discuss_post`;
CREATE TABLE `discuss_post` (
  `id` int NOT NULL AUTO_INCREMENT,
  `user_id` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
  `title` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
  `content` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci,
  `type` int DEFAULT NULL COMMENT '0-普通；1-置顶；',
  `status` int DEFAULT NULL,
  `create_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `score` double DEFAULT NULL,
  `board_id` bigint NOT NULL DEFAULT '0' COMMENT '所属板块ID',
  PRIMARY KEY (`id`),
  KEY `index_user_id` (`user_id`)
) ENGINE=InnoDB AUTO_INCREMENT=74 DEFAULT CHARSET=latin1;

-- ----------------------------
-- Records of discuss_post
-- ----------------------------
BEGIN;
INSERT INTO `discuss_post` (`id`, `user_id`, `title`, `content`, `type`, `status`, `create_time`, `score`, `board_id`) VALUES (67, '19', 'test1', 'this is the first test', 0, 1, '2025-05-23 02:04:05', 0, 2);
INSERT INTO `discuss_post` (`id`, `user_id`, `title`, `content`, `type`, `status`, `create_time`, `score`, `board_id`) VALUES (68, '24', 'test2', 'This is the second test\nWith some fonts', 0, 1, '2025-05-23 13:07:12', 0, 2);
INSERT INTO `discuss_post` (`id`, `user_id`, `title`, `content`, `type`, `status`, `create_time`, `score`, `board_id`) VALUES (69, '24', 'FeedBack', 'Hey Maintainers,\nIn this post i\'d like to communicate the feedback for this website/forum.\n\nFirst Impressions:\nSeems nice. Registering and logging in went smoothly. The captcha only works when typing in lowercase even though the image displays uppercase. Am curious about the back end so far. \n\nAlso a few questions:\n\n- Why have you made this website?\n- How long do you intend to have this Testing phase to be?\n- What do you intend with this website?\n\n(I\'ll update this post somewhat regularly with my own feedback and those given in the comments)\n\nedit1: Spelling.\nedit2: Post nmbr \'nice\'', 3, 1, '2025-05-23 13:15:35', 0, 1);
INSERT INTO `discuss_post` (`id`, `user_id`, `title`, `content`, `type`, `status`, `create_time`, `score`, `board_id`) VALUES (70, '19', 'Read Me', '**Hi I am Gao, the developer of the whole forum. Here is some instruction.**\n\n1. To write a `Post` or `Comment` , you have to have an **account** and **verify your email**\n2. After login, you will be given `User Role` permission and `basic title` for beginner (You can see the detail in your *profile Page*)\n   * Click the `title` in your proifile page to change to an other one if you have\n   * Cliek the summary of your total `comment` or `post` you will jump to a `summary page` for the entity clicked\n   * And also you can edit your profile in this page.\n3. Remembre, for **the post** or any **comment to the post**, you can use *MarkDown* to write whatever you like, also you can add any **img** with it\'s `link`\n4. When otherone gives you a like at your `comment` or `post` you will be noticed. And you can see a red dot on your icon of your avatar. Then go to the `notice` page, you can see all your notifications.\n    * The button `read for all` only works for *notification not by system*\n    * When you get a new title, you will be notified also by system.\n    * For any information like\n    ```\n       likes to your comment or floor, reply to your comment floor, reply to your comment in the floor, notice of admin etc.\n    ```\n    you will receive a notification\n\n5. In any part of the page in any board, if you see a username and you wanna know the information of it, *click it* , it will show the basic information card of the username clicked. **( ps: Only works when you have already logeded in)**\n6. If you wanna experience all functions, please contact me with ***my email in the footer***.\n\n> Try it now !!!!!!!!!!!!!!\n\n![Description](https://media.tenor.com/AZ9zBjp71HIAAAAM/supernatural-jensen-ackles.gif)', 3, 1, '2025-05-24 23:10:38', 0, 2);
INSERT INTO `discuss_post` (`id`, `user_id`, `title`, `content`, `type`, `status`, `create_time`, `score`, `board_id`) VALUES (71, '25', 'hi', '#include<unistd.h>\nvoid', 0, 1, '2025-05-25 01:24:52', 0, 2);
INSERT INTO `discuss_post` (`id`, `user_id`, `title`, `content`, `type`, `status`, `create_time`, `score`, `board_id`) VALUES (72, '25', 'ii', '<>/n\njajaj/n\n', 3, 0, '2025-05-25 01:25:45', 0, 2);
INSERT INTO `discuss_post` (`id`, `user_id`, `title`, `content`, `type`, `status`, `create_time`, `score`, `board_id`) VALUES (73, '19', 'Read Me First', '**Hi I am Gao, the developer of the whole forum. Here is some instruction.**\n\n1. To write a `Post` or `Comment` , you have to have an **account** and **verify your email**\n2. After login, you will be given `User Role` permission and `basic title` for beginner (You can see the detail in your *profile Page*)\n   * Click the `title` in your proifile page to change to an other one if you have\n   * Cliek the summary of your total `comment` or `post` you will jump to a `summary page` for the entity clicked\n   * And also you can edit your profile in this page.\n3. Remembre, for **the post** or any **comment to the post**, you can use *MarkDown* to write whatever you like, also you can add any **img** with it\'s `link`\n4. When otherone gives you a like at your `comment` or `post` you will be noticed. And you can see a red dot on your icon of your avatar. Then go to the `notice` page, you can see all your notifications.\n    * The button `read for all` only works for *notification not by system*\n    * When you get a new title, you will be notified also by system.\n    * For any information like\n    ```\n       likes to your comment or floor, reply to your comment floor, reply to your comment in the floor, notice of admin etc.\n    ```\n    you will receive a notification\n\n5. In any part of the page in any board, if you see a username and you wanna know the information of it, *click it* , it will show the basic information card of the username clicked. **( ps: Only works when you have already logeded in)**\n6. If you wanna experience all functions, please contact me with ***my email in the footer***.\n\n> Try it now !!!!!!!!!!!!!!\n\n![Description](https://media.tenor.com/AZ9zBjp71HIAAAAM/supernatural-jensen-ackles.gif)\n\n## 📤 Upload File Module\n\nBecause I\'m currently broke 💸, I can\'t afford to support native file uploads yet.  \nSo for now, **please upload your file to your own cloud drive or GitHub and just share the link**.  \n\n> ![](https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQ_lYdqVblVdeWga9PqSVKnRXPFkPfY2y-bjQ&s)  \n> *I\'m doing my best on a tight budget, okay?*\n\n---\n\n## 🛠 Section Creation Module\n\nI\'m still working on this part.  \nInstead of simply giving section creation rights to admins, I want to make it more **fun and engaging**.\n\nMy goal:  \n- Make it feel like you\'re **giving birth to a new idea** 🐣  \n- Maybe even involve the community — voting, suggestions, etc.  \n- Let section creation feel special, not just \"admin power\"\n\n> ![](https://p5.itc.cn/q_70/images03/20210219/4a1bbb56adc548de9525178250193a1a.gif)  \n> *Me trying to design the perfect section creation experience.*\n\n---\n\n## 📬 Private Message Module (Maybe do this as a live-chat)\n\nThis one might take a while, because I want to do it **properly**:\n\n1. Design a solid message table structure in the database 📚  \n2. Implement **unread message reminders** 🔔  \n3. Add control over **who can send messages** — I don’t want spam flooding the system\n\nWhat I’m planning:  \n- Only allow DMs from mutual followers (Maybe)  \n- Add block/report functionality  \n- Notifications for new and unread messages  \n\n> ![](https://www.ntet.cn/uploads/images/20220217/20220217133401_96452.png)  \n> *If I don’t put restrictions on messaging, this will be everyone’s inbox.*\n\n---\n\nThanks for your patience 💖 I promise I’m building it with love and caffeine ☕\n', 3, 1, '2025-05-26 02:03:26', 0, 1);
COMMIT;

-- ----------------------------
-- Table structure for notification
-- ----------------------------
DROP TABLE IF EXISTS `notification`;
CREATE TABLE `notification` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `sender_id` bigint DEFAULT NULL,
  `type` int NOT NULL DEFAULT '0',
  `target_id` bigint DEFAULT NULL,
  `status` int NOT NULL DEFAULT '0',
  `content` text,
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP,
  `title` varchar(50) DEFAULT NULL,
  `entity_id` int DEFAULT NULL,
  `entity_type` int DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=254 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='通知事件表';

-- ----------------------------
-- Records of notification
-- ----------------------------
BEGIN;
INSERT INTO `notification` (`id`, `sender_id`, `type`, `target_id`, `status`, `content`, `create_time`, `title`, `entity_id`, `entity_type`) VALUES (223, 23, 6, 23, 1, 'You have been granted a new title: Greenhorn', '2025-05-23 00:28:25', 'New title has been granted', 1, 3);
INSERT INTO `notification` (`id`, `sender_id`, `type`, `target_id`, `status`, `content`, `create_time`, `title`, `entity_id`, `entity_type`) VALUES (224, 23, 2, 19, 1, NULL, '2025-05-23 02:00:59', NULL, 67, 1);
INSERT INTO `notification` (`id`, `sender_id`, `type`, `target_id`, `status`, `content`, `create_time`, `title`, `entity_id`, `entity_type`) VALUES (225, 24, 6, 24, 1, 'You have been granted a new title: Greenhorn', '2025-05-23 11:04:39', 'New title has been granted', 1, 3);
INSERT INTO `notification` (`id`, `sender_id`, `type`, `target_id`, `status`, `content`, `create_time`, `title`, `entity_id`, `entity_type`) VALUES (226, 19, 2, 24, 1, NULL, '2025-05-23 11:14:47', NULL, 68, 1);
INSERT INTO `notification` (`id`, `sender_id`, `type`, `target_id`, `status`, `content`, `create_time`, `title`, `entity_id`, `entity_type`) VALUES (227, 19, 0, 24, 1, NULL, '2025-05-23 11:15:13', NULL, 271, 2);
INSERT INTO `notification` (`id`, `sender_id`, `type`, `target_id`, `status`, `content`, `create_time`, `title`, `entity_id`, `entity_type`) VALUES (228, 19, 1, 24, 1, NULL, '2025-05-23 11:15:38', NULL, 272, 2);
INSERT INTO `notification` (`id`, `sender_id`, `type`, `target_id`, `status`, `content`, `create_time`, `title`, `entity_id`, `entity_type`) VALUES (229, 19, 1, 24, 1, NULL, '2025-05-23 12:04:38', NULL, 273, 2);
INSERT INTO `notification` (`id`, `sender_id`, `type`, `target_id`, `status`, `content`, `create_time`, `title`, `entity_id`, `entity_type`) VALUES (230, 19, 2, 24, 1, NULL, '2025-05-23 12:05:19', NULL, 69, 1);
INSERT INTO `notification` (`id`, `sender_id`, `type`, `target_id`, `status`, `content`, `create_time`, `title`, `entity_id`, `entity_type`) VALUES (231, 19, 1, 24, 1, NULL, '2025-05-23 12:09:02', NULL, 274, 2);
INSERT INTO `notification` (`id`, `sender_id`, `type`, `target_id`, `status`, `content`, `create_time`, `title`, `entity_id`, `entity_type`) VALUES (232, 19, 0, 24, 1, NULL, '2025-05-23 12:14:17', NULL, 275, 2);
INSERT INTO `notification` (`id`, `sender_id`, `type`, `target_id`, `status`, `content`, `create_time`, `title`, `entity_id`, `entity_type`) VALUES (233, 19, 0, 24, 1, NULL, '2025-05-23 12:14:17', NULL, 276, 2);
INSERT INTO `notification` (`id`, `sender_id`, `type`, `target_id`, `status`, `content`, `create_time`, `title`, `entity_id`, `entity_type`) VALUES (234, 19, 0, 24, 1, NULL, '2025-05-23 12:19:49', NULL, 277, 2);
INSERT INTO `notification` (`id`, `sender_id`, `type`, `target_id`, `status`, `content`, `create_time`, `title`, `entity_id`, `entity_type`) VALUES (235, 24, 2, 19, 1, NULL, '2025-05-23 12:20:30', NULL, 273, 2);
INSERT INTO `notification` (`id`, `sender_id`, `type`, `target_id`, `status`, `content`, `create_time`, `title`, `entity_id`, `entity_type`) VALUES (236, 24, 2, 19, 1, NULL, '2025-05-23 12:20:37', NULL, 274, 2);
INSERT INTO `notification` (`id`, `sender_id`, `type`, `target_id`, `status`, `content`, `create_time`, `title`, `entity_id`, `entity_type`) VALUES (237, 19, 0, 24, 1, NULL, '2025-05-23 12:20:40', NULL, 278, 2);
INSERT INTO `notification` (`id`, `sender_id`, `type`, `target_id`, `status`, `content`, `create_time`, `title`, `entity_id`, `entity_type`) VALUES (238, 24, 2, 19, 1, NULL, '2025-05-23 12:28:46', NULL, 275, 2);
INSERT INTO `notification` (`id`, `sender_id`, `type`, `target_id`, `status`, `content`, `create_time`, `title`, `entity_id`, `entity_type`) VALUES (239, 24, 2, 19, 1, NULL, '2025-05-23 12:28:47', NULL, 277, 2);
INSERT INTO `notification` (`id`, `sender_id`, `type`, `target_id`, `status`, `content`, `create_time`, `title`, `entity_id`, `entity_type`) VALUES (240, 24, 0, 19, 1, NULL, '2025-05-23 12:30:42', NULL, 280, 2);
INSERT INTO `notification` (`id`, `sender_id`, `type`, `target_id`, `status`, `content`, `create_time`, `title`, `entity_id`, `entity_type`) VALUES (241, 19, 0, 24, 1, NULL, '2025-05-23 12:34:17', NULL, 281, 2);
INSERT INTO `notification` (`id`, `sender_id`, `type`, `target_id`, `status`, `content`, `create_time`, `title`, `entity_id`, `entity_type`) VALUES (242, 19, 2, 24, 1, NULL, '2025-05-23 12:34:30', NULL, 280, 2);
INSERT INTO `notification` (`id`, `sender_id`, `type`, `target_id`, `status`, `content`, `create_time`, `title`, `entity_id`, `entity_type`) VALUES (243, 19, 5, 0, 0, 'Bro I just give you permission of admin. You can see your menu. ', '2025-05-23 12:35:50', 'Notice', 0, 0);
INSERT INTO `notification` (`id`, `sender_id`, `type`, `target_id`, `status`, `content`, `create_time`, `title`, `entity_id`, `entity_type`) VALUES (244, 19, 0, 24, 1, NULL, '2025-05-23 12:40:29', NULL, 282, 2);
INSERT INTO `notification` (`id`, `sender_id`, `type`, `target_id`, `status`, `content`, `create_time`, `title`, `entity_id`, `entity_type`) VALUES (245, 25, 6, 25, 1, 'You have been granted a new title: Greenhorn', '2025-05-24 23:21:34', 'New title has been granted', 1, 3);
INSERT INTO `notification` (`id`, `sender_id`, `type`, `target_id`, `status`, `content`, `create_time`, `title`, `entity_id`, `entity_type`) VALUES (246, 23, 2, 19, 1, NULL, '2025-05-24 23:24:46', NULL, 70, 1);
INSERT INTO `notification` (`id`, `sender_id`, `type`, `target_id`, `status`, `content`, `create_time`, `title`, `entity_id`, `entity_type`) VALUES (247, 23, 0, 19, 1, NULL, '2025-05-24 23:25:06', NULL, 285, 2);
INSERT INTO `notification` (`id`, `sender_id`, `type`, `target_id`, `status`, `content`, `create_time`, `title`, `entity_id`, `entity_type`) VALUES (248, 19, 2, 25, 1, NULL, '2025-05-24 23:26:25', NULL, 72, 1);
INSERT INTO `notification` (`id`, `sender_id`, `type`, `target_id`, `status`, `content`, `create_time`, `title`, `entity_id`, `entity_type`) VALUES (249, 19, 2, 25, 1, NULL, '2025-05-24 23:26:30', NULL, 71, 1);
INSERT INTO `notification` (`id`, `sender_id`, `type`, `target_id`, `status`, `content`, `create_time`, `title`, `entity_id`, `entity_type`) VALUES (250, 25, 2, 19, 1, NULL, '2025-05-24 23:31:01', NULL, 283, 2);
INSERT INTO `notification` (`id`, `sender_id`, `type`, `target_id`, `status`, `content`, `create_time`, `title`, `entity_id`, `entity_type`) VALUES (251, 19, 5, 0, 0, '略略略略略略略略略', '2025-05-25 00:47:40', '哈哈哈', 0, 0);
INSERT INTO `notification` (`id`, `sender_id`, `type`, `target_id`, `status`, `content`, `create_time`, `title`, `entity_id`, `entity_type`) VALUES (252, 25, 5, 0, 0, 'ください　みず　バンがおじ　すし　です', '2025-05-25 00:48:55', 'んでkjfんd', 0, 0);
INSERT INTO `notification` (`id`, `sender_id`, `type`, `target_id`, `status`, `content`, `create_time`, `title`, `entity_id`, `entity_type`) VALUES (253, 26, 6, 26, 1, 'You have been granted a new title: Greenhorn', '2025-05-25 12:05:28', 'New title has been granted', 1, 3);
COMMIT;

-- ----------------------------
-- Table structure for notification_read
-- ----------------------------
DROP TABLE IF EXISTS `notification_read`;
CREATE TABLE `notification_read` (
  `notification_id` bigint NOT NULL,
  `user_id` bigint NOT NULL,
  `read_time` datetime DEFAULT NULL,
  PRIMARY KEY (`notification_id`,`user_id`),
  KEY `user_id` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='用户通知读取状态表';

-- ----------------------------
-- Records of notification_read
-- ----------------------------
BEGIN;
INSERT INTO `notification_read` (`notification_id`, `user_id`, `read_time`) VALUES (223, 23, '2025-05-23 00:28:39');
INSERT INTO `notification_read` (`notification_id`, `user_id`, `read_time`) VALUES (224, 19, '2025-05-23 02:01:30');
INSERT INTO `notification_read` (`notification_id`, `user_id`, `read_time`) VALUES (225, 24, '2025-05-23 12:39:04');
INSERT INTO `notification_read` (`notification_id`, `user_id`, `read_time`) VALUES (226, 24, '2025-05-23 12:39:04');
INSERT INTO `notification_read` (`notification_id`, `user_id`, `read_time`) VALUES (227, 24, '2025-05-23 12:39:04');
INSERT INTO `notification_read` (`notification_id`, `user_id`, `read_time`) VALUES (228, 24, '2025-05-23 12:39:04');
INSERT INTO `notification_read` (`notification_id`, `user_id`, `read_time`) VALUES (229, 24, '2025-05-23 12:39:04');
INSERT INTO `notification_read` (`notification_id`, `user_id`, `read_time`) VALUES (230, 24, '2025-05-23 12:39:04');
INSERT INTO `notification_read` (`notification_id`, `user_id`, `read_time`) VALUES (231, 24, '2025-05-23 12:39:04');
INSERT INTO `notification_read` (`notification_id`, `user_id`, `read_time`) VALUES (232, 24, '2025-05-23 12:39:04');
INSERT INTO `notification_read` (`notification_id`, `user_id`, `read_time`) VALUES (233, 24, '2025-05-23 12:39:04');
INSERT INTO `notification_read` (`notification_id`, `user_id`, `read_time`) VALUES (234, 24, '2025-05-23 12:39:04');
INSERT INTO `notification_read` (`notification_id`, `user_id`, `read_time`) VALUES (235, 19, '2025-05-23 12:21:06');
INSERT INTO `notification_read` (`notification_id`, `user_id`, `read_time`) VALUES (236, 19, '2025-05-23 12:21:06');
INSERT INTO `notification_read` (`notification_id`, `user_id`, `read_time`) VALUES (237, 24, '2025-05-23 12:39:04');
INSERT INTO `notification_read` (`notification_id`, `user_id`, `read_time`) VALUES (238, 19, '2025-05-23 12:36:13');
INSERT INTO `notification_read` (`notification_id`, `user_id`, `read_time`) VALUES (239, 19, '2025-05-23 12:36:13');
INSERT INTO `notification_read` (`notification_id`, `user_id`, `read_time`) VALUES (240, 19, '2025-05-23 12:36:13');
INSERT INTO `notification_read` (`notification_id`, `user_id`, `read_time`) VALUES (241, 24, '2025-05-23 12:39:04');
INSERT INTO `notification_read` (`notification_id`, `user_id`, `read_time`) VALUES (242, 24, '2025-05-23 12:39:04');
INSERT INTO `notification_read` (`notification_id`, `user_id`, `read_time`) VALUES (243, 19, '2025-05-23 12:36:07');
INSERT INTO `notification_read` (`notification_id`, `user_id`, `read_time`) VALUES (246, 19, '2025-05-24 23:27:38');
INSERT INTO `notification_read` (`notification_id`, `user_id`, `read_time`) VALUES (247, 19, '2025-05-24 23:27:38');
INSERT INTO `notification_read` (`notification_id`, `user_id`, `read_time`) VALUES (250, 19, '2025-05-25 00:48:12');
COMMIT;

-- ----------------------------
-- Table structure for permission
-- ----------------------------
DROP TABLE IF EXISTS `permission`;
CREATE TABLE `permission` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `code` varchar(100) NOT NULL COMMENT '权限标识，如 post:delete_any',
  `name` varchar(50) NOT NULL COMMENT '展示名，如 删除任意帖子',
  `description` varchar(255) DEFAULT NULL,
  `method` varchar(10) DEFAULT NULL,
  `url_pattern` varchar(200) DEFAULT NULL,
  `status` tinyint DEFAULT '1',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP,
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `level` int DEFAULT '0',
  PRIMARY KEY (`id`),
  UNIQUE KEY `code` (`code`)
) ENGINE=InnoDB AUTO_INCREMENT=110 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- ----------------------------
-- Records of permission
-- ----------------------------
BEGIN;
INSERT INTO `permission` (`id`, `code`, `name`, `description`, `method`, `url_pattern`, `status`, `create_time`, `update_time`, `level`) VALUES (1, 'user:logout:any', '强制用户下线', '管理员强制指定用户退出登录', 'DELETE', '/admin/user/logout/{userId}', 1, '2025-05-03 19:46:54', '2025-05-09 18:14:08', 0);
INSERT INTO `permission` (`id`, `code`, `name`, `description`, `method`, `url_pattern`, `status`, `create_time`, `update_time`, `level`) VALUES (2, 'comment:publish:topost', '回复帖子', '对帖子发表评论', 'POST', '/comment/topost', 1, '2025-05-03 19:46:54', '2025-05-10 22:43:02', 3);
INSERT INTO `permission` (`id`, `code`, `name`, `description`, `method`, `url_pattern`, `status`, `create_time`, `update_time`, `level`) VALUES (3, 'comment:publish:tocomment', '回复评论', '对评论进行回复或@他人', 'POST', '/comment/tocomment', 1, '2025-05-03 19:46:54', '2025-05-10 22:43:05', 3);
INSERT INTO `permission` (`id`, `code`, `name`, `description`, `method`, `url_pattern`, `status`, `create_time`, `update_time`, `level`) VALUES (4, 'comment:delete:own', '删除自己评论', '删除自己发布的评论', 'DELETE', '/comment/{id}', 1, '2025-05-03 19:46:54', '2025-05-10 22:43:07', 3);
INSERT INTO `permission` (`id`, `code`, `name`, `description`, `method`, `url_pattern`, `status`, `create_time`, `update_time`, `level`) VALUES (5, 'comment:update:own', '修改自己评论', '修改自己发布的评论', 'PUT', '/comment/update', 1, '2025-05-03 19:46:54', '2025-05-10 22:43:10', 3);
INSERT INTO `permission` (`id`, `code`, `name`, `description`, `method`, `url_pattern`, `status`, `create_time`, `update_time`, `level`) VALUES (6, 'comment:info:board', '查看板块评论', '贴主查看本板块下所有评论', 'GET', '/admin/comment/info/{boardId}', 1, '2025-05-03 19:46:54', '2025-05-10 22:43:20', 2);
INSERT INTO `permission` (`id`, `code`, `name`, `description`, `method`, `url_pattern`, `status`, `create_time`, `update_time`, `level`) VALUES (7, 'comment:info:any', '查看所有评论', '管理员查看所有评论', 'GET', '/admin/comment/info/{boardId}', 1, '2025-05-03 19:46:54', '2025-05-10 22:43:29', 1);
INSERT INTO `permission` (`id`, `code`, `name`, `description`, `method`, `url_pattern`, `status`, `create_time`, `update_time`, `level`) VALUES (8, 'comment:delete:board', '删除板块评论', '贴主删除本板块下的评论', 'DELETE', '/admin/comment/{id}', 1, '2025-05-03 19:46:54', '2025-05-10 22:43:50', 2);
INSERT INTO `permission` (`id`, `code`, `name`, `description`, `method`, `url_pattern`, `status`, `create_time`, `update_time`, `level`) VALUES (9, 'comment:delete:any', '删除任意评论', '管理员删除任意评论', 'DELETE', '/admin/comment/{id}', 1, '2025-05-03 19:46:54', '2025-05-10 22:43:52', 1);
INSERT INTO `permission` (`id`, `code`, `name`, `description`, `method`, `url_pattern`, `status`, `create_time`, `update_time`, `level`) VALUES (10, 'post:publish', '发布帖子', '创建新帖子', 'POST', '/post/publish', 1, '2025-05-03 19:46:54', '2025-05-10 22:43:56', 3);
INSERT INTO `permission` (`id`, `code`, `name`, `description`, `method`, `url_pattern`, `status`, `create_time`, `update_time`, `level`) VALUES (11, 'post:delete:own', '删除自己帖子', '删除自己发布的帖子', 'DELETE', '/post/{id}', 1, '2025-05-03 19:46:54', '2025-05-10 22:43:59', 3);
INSERT INTO `permission` (`id`, `code`, `name`, `description`, `method`, `url_pattern`, `status`, `create_time`, `update_time`, `level`) VALUES (12, 'post:update:own', '修改自己帖子', '修改自己发布的帖子', 'PUT', '/post/update', 1, '2025-05-03 19:46:54', '2025-05-10 22:44:02', 3);
INSERT INTO `permission` (`id`, `code`, `name`, `description`, `method`, `url_pattern`, `status`, `create_time`, `update_time`, `level`) VALUES (15, 'post:delete:board', '删除板块帖子', '贴主删除本板块帖子', 'DELETE', '/admin/post/{id}', 1, '2025-05-03 19:46:54', '2025-05-10 22:44:20', 2);
INSERT INTO `permission` (`id`, `code`, `name`, `description`, `method`, `url_pattern`, `status`, `create_time`, `update_time`, `level`) VALUES (16, 'post:delete:any', '删除任意帖子', '管理员删除任意帖子', 'DELETE', '/admin/post/{id}', 1, '2025-05-03 19:46:54', '2025-05-10 22:44:25', 1);
INSERT INTO `permission` (`id`, `code`, `name`, `description`, `method`, `url_pattern`, `status`, `create_time`, `update_time`, `level`) VALUES (17, 'user:info:own', '查看用户信息', '用户查看自己信息或公开信息', 'GET', '/user/info', 1, '2025-05-03 19:46:54', '2025-05-10 22:44:26', 3);
INSERT INTO `permission` (`id`, `code`, `name`, `description`, `method`, `url_pattern`, `status`, `create_time`, `update_time`, `level`) VALUES (18, 'user:password:change:own', '修改自己密码', '用户修改自己的登录密码', 'PUT', '/user/change-password', 1, '2025-05-03 19:46:54', '2025-05-10 22:44:30', 3);
INSERT INTO `permission` (`id`, `code`, `name`, `description`, `method`, `url_pattern`, `status`, `create_time`, `update_time`, `level`) VALUES (19, 'user:password:change:any', '修改任意用户密码', '管理员修改任意用户密码', 'PUT', '/admin/user/change-password/{userId}', 1, '2025-05-03 19:46:54', '2025-05-09 19:36:58', 0);
INSERT INTO `permission` (`id`, `code`, `name`, `description`, `method`, `url_pattern`, `status`, `create_time`, `update_time`, `level`) VALUES (20, 'user:info:any', '查看任意用户信息', '管理员产看任意用户信息, 前端根据这个确定是否显示管理员菜单', 'GET', '/admin/user/info/{id}', 1, '2025-05-04 23:08:54', '2025-05-10 22:45:29', 1);
INSERT INTO `permission` (`id`, `code`, `name`, `description`, `method`, `url_pattern`, `status`, `create_time`, `update_time`, `level`) VALUES (21, 'user:activate:any', '启用任意用户', '管理员启用任意用户账户', 'PUT', '/admin/user/activate/{userId}', 1, '2025-05-05 22:50:18', '2025-05-10 22:45:36', 1);
INSERT INTO `permission` (`id`, `code`, `name`, `description`, `method`, `url_pattern`, `status`, `create_time`, `update_time`, `level`) VALUES (22, 'user:disable:any', '禁用任意用户', '管理员禁用任意用户账户', 'PUT', '/admin/user/disable/{userId}', 1, '2025-05-05 22:50:18', '2025-05-10 22:45:40', 1);
INSERT INTO `permission` (`id`, `code`, `name`, `description`, `method`, `url_pattern`, `status`, `create_time`, `update_time`, `level`) VALUES (23, 'user:delete:any', '删除任意用户', '管理员删除任意用户账户', 'DELETE', '/admin/user/delete/{userId}', 1, '2025-05-05 22:50:18', '2025-05-05 22:50:18', 0);
INSERT INTO `permission` (`id`, `code`, `name`, `description`, `method`, `url_pattern`, `status`, `create_time`, `update_time`, `level`) VALUES (25, 'user:username-change:own', '修改用户名', '修改自己用户名', 'PUT', '/user/change-username', 1, '2025-05-09 17:58:44', '2025-05-10 22:45:50', 3);
INSERT INTO `permission` (`id`, `code`, `name`, `description`, `method`, `url_pattern`, `status`, `create_time`, `update_time`, `level`) VALUES (26, 'user:update-info:own', '修改基础信息', '修改自己基础信息', 'PUT', '/user/info', 1, '2025-05-09 17:58:44', '2025-05-10 22:45:54', 3);
INSERT INTO `permission` (`id`, `code`, `name`, `description`, `method`, `url_pattern`, `status`, `create_time`, `update_time`, `level`) VALUES (67, 'post:change-type:board', '版主加精或者置顶', '板主加精或者指定帖子', 'PUT', '/post/change-type', 1, '2025-05-20 02:02:40', '2025-05-20 02:07:49', 2);
INSERT INTO `permission` (`id`, `code`, `name`, `description`, `method`, `url_pattern`, `status`, `create_time`, `update_time`, `level`) VALUES (68, 'post:change-type:any', '管理员加精或者置顶', '管理员加精或者指定帖子', 'PUT', '/post/change-type', 1, '2025-05-20 02:02:40', '2025-05-20 02:08:04', 1);
INSERT INTO `permission` (`id`, `code`, `name`, `description`, `method`, `url_pattern`, `status`, `create_time`, `update_time`, `level`) VALUES (69, 'like:post', '点赞帖子', '点赞帖子的权利', 'POST', '/like/post/{postId}', 1, '2025-05-20 18:11:00', '2025-05-20 18:11:00', 3);
INSERT INTO `permission` (`id`, `code`, `name`, `description`, `method`, `url_pattern`, `status`, `create_time`, `update_time`, `level`) VALUES (70, 'like:comment', '点赞评论', '点赞评论的权利', 'POST', '/like/comment/{commentId}', 1, '2025-05-20 18:11:00', '2025-05-20 18:11:00', 3);
INSERT INTO `permission` (`id`, `code`, `name`, `description`, `method`, `url_pattern`, `status`, `create_time`, `update_time`, `level`) VALUES (71, 'notification:all:own', '获取自己所有通知', '用户可以读取自己所有通知', 'GET', '/notification', 1, '2025-05-20 18:11:00', '2025-05-20 18:11:00', 3);
INSERT INTO `permission` (`id`, `code`, `name`, `description`, `method`, `url_pattern`, `status`, `create_time`, `update_time`, `level`) VALUES (72, 'notification:unread:own', '获取自己未读通知', '用户可以读取所有未读通知', 'GET', '/notification/unread', 1, '2025-05-20 18:11:00', '2025-05-20 18:11:00', 3);
INSERT INTO `permission` (`id`, `code`, `name`, `description`, `method`, `url_pattern`, `status`, `create_time`, `update_time`, `level`) VALUES (73, 'notification:read:own', '获取自己已读通知', '用户可以读取所有已读通知', 'GET', '/notification/read', 1, '2025-05-20 18:11:00', '2025-05-20 18:11:00', 3);
INSERT INTO `permission` (`id`, `code`, `name`, `description`, `method`, `url_pattern`, `status`, `create_time`, `update_time`, `level`) VALUES (74, 'notification:mark-read-all:own', '标记所有为已读', '可以标记除了系统通知外的所有通知为已读', 'PUT', '/notification', 1, '2025-05-20 18:11:00', '2025-05-20 18:11:00', 3);
INSERT INTO `permission` (`id`, `code`, `name`, `description`, `method`, `url_pattern`, `status`, `create_time`, `update_time`, `level`) VALUES (75, 'notification:mark-read-system:own', '标记系统通知已读', '标记系统通知为已读', 'PUT', '/notification/{id}', 1, '2025-05-20 18:11:00', '2025-05-20 18:11:00', 3);
INSERT INTO `permission` (`id`, `code`, `name`, `description`, `method`, `url_pattern`, `status`, `create_time`, `update_time`, `level`) VALUES (76, 'statistics:weekly-activity', '周活跃统计', '获取周活跃统计表格数据', 'GET', '/statistics/weekly-activity', 1, '2025-05-20 18:11:00', '2025-05-20 18:11:00', 3);
INSERT INTO `permission` (`id`, `code`, `name`, `description`, `method`, `url_pattern`, `status`, `create_time`, `update_time`, `level`) VALUES (77, 'statistics:monthly-registration', '月注册统计', '获取月注册统计表格数据', 'GET', '/statistics/monthly-registration', 1, '2025-05-20 18:11:00', '2025-05-20 18:11:00', 3);
INSERT INTO `permission` (`id`, `code`, `name`, `description`, `method`, `url_pattern`, `status`, `create_time`, `update_time`, `level`) VALUES (78, 'user:comments', '查看自己的评论', '查看自己的所有评论', 'GET', '/user/comments', 1, '2025-05-20 18:11:00', '2025-05-20 18:11:00', 3);
INSERT INTO `permission` (`id`, `code`, `name`, `description`, `method`, `url_pattern`, `status`, `create_time`, `update_time`, `level`) VALUES (79, 'user:posts', '查看自己或其他用户的帖子', '查看自己或其他用户的所有帖子', 'GET', '/user/posts', 1, '2025-05-20 18:11:00', '2025-05-20 18:11:00', 3);
INSERT INTO `permission` (`id`, `code`, `name`, `description`, `method`, `url_pattern`, `status`, `create_time`, `update_time`, `level`) VALUES (80, 'sys-notification:info:any', '获取系统通知', '获取系统通知的权利', 'GET', '/admin/notification', 1, '2025-05-20 18:36:23', '2025-05-20 18:36:23', 1);
INSERT INTO `permission` (`id`, `code`, `name`, `description`, `method`, `url_pattern`, `status`, `create_time`, `update_time`, `level`) VALUES (81, 'sys-notification:publish:any', '发布系统通知', '发布系统通知的权利', 'POST', '/admin/notification', 1, '2025-05-20 18:36:23', '2025-05-20 18:36:23', 1);
INSERT INTO `permission` (`id`, `code`, `name`, `description`, `method`, `url_pattern`, `status`, `create_time`, `update_time`, `level`) VALUES (82, 'sys-notification:modify:any', '修改系统通知', '修改系统通知的权利', 'PUT', '/admin/notification', 1, '2025-05-20 18:36:23', '2025-05-20 18:36:23', 1);
INSERT INTO `permission` (`id`, `code`, `name`, `description`, `method`, `url_pattern`, `status`, `create_time`, `update_time`, `level`) VALUES (83, 'sys-notification:delete:any', '删除系统通知', '删除系统通知的权利', 'DELETE', '/admin/notification/{id}', 1, '2025-05-20 18:36:23', '2025-05-20 18:36:23', 1);
INSERT INTO `permission` (`id`, `code`, `name`, `description`, `method`, `url_pattern`, `status`, `create_time`, `update_time`, `level`) VALUES (84, 'permission:role-detail:any', '获取角色与权限信息', '只会显示权限比自己低的角色信息', 'GET', '/permission/role-detail', 1, '2025-05-20 18:59:42', '2025-05-20 18:59:42', 1);
INSERT INTO `permission` (`id`, `code`, `name`, `description`, `method`, `url_pattern`, `status`, `create_time`, `update_time`, `level`) VALUES (85, 'permission:update-user-role:any', '给用户分配角色', '可以给同个用户分配多个角色', 'PUT', '/permission/user-role', 1, '2025-05-20 18:59:42', '2025-05-20 18:59:42', 1);
INSERT INTO `permission` (`id`, `code`, `name`, `description`, `method`, `url_pattern`, `status`, `create_time`, `update_time`, `level`) VALUES (86, 'permission:update-role-permission:any', '改变角色的权限', '改变角色和权限的对应关系', 'PUT', '/permission/role-permission', 1, '2025-05-20 18:59:42', '2025-05-20 18:59:42', 1);
INSERT INTO `permission` (`id`, `code`, `name`, `description`, `method`, `url_pattern`, `status`, `create_time`, `update_time`, `level`) VALUES (87, 'permission:add-role-permission:any', '添加新角色', '添加新的角色以及其对应的权限关系', 'POST', '/permission/role-permission', 1, '2025-05-20 18:59:42', '2025-05-20 18:59:42', 1);
INSERT INTO `permission` (`id`, `code`, `name`, `description`, `method`, `url_pattern`, `status`, `create_time`, `update_time`, `level`) VALUES (88, 'permission:delete-role:any', '删除角色', '删除角色', 'DELETE', '/permission/role/{id}', 1, '2025-05-20 18:59:42', '2025-05-20 18:59:42', 1);
INSERT INTO `permission` (`id`, `code`, `name`, `description`, `method`, `url_pattern`, `status`, `create_time`, `update_time`, `level`) VALUES (89, 'carousel:add:any', '添加轮播图', '增加新的轮播图', 'POST', '/layout/carousel', 1, '2025-05-20 19:20:35', '2025-05-20 19:20:35', 0);
INSERT INTO `permission` (`id`, `code`, `name`, `description`, `method`, `url_pattern`, `status`, `create_time`, `update_time`, `level`) VALUES (90, 'carousel:update:any', '修改轮播图', '修改轮播图', 'PUT', '/layout/carousel', 1, '2025-05-20 19:20:35', '2025-05-20 19:20:35', 0);
INSERT INTO `permission` (`id`, `code`, `name`, `description`, `method`, `url_pattern`, `status`, `create_time`, `update_time`, `level`) VALUES (91, 'carousel:delete:any', '删除轮播图', '删除轮播图', 'DELETE', '/layout/carousel/{id}', 1, '2025-05-20 19:20:35', '2025-05-20 19:20:35', 0);
INSERT INTO `permission` (`id`, `code`, `name`, `description`, `method`, `url_pattern`, `status`, `create_time`, `update_time`, `level`) VALUES (92, 'board:detail:any', '查看板块详情', '管理员获取人板块详细信息', 'GET', '/board/detail', 1, '2025-05-27 22:47:04', '2025-05-27 22:47:04', 1);
INSERT INTO `permission` (`id`, `code`, `name`, `description`, `method`, `url_pattern`, `status`, `create_time`, `update_time`, `level`) VALUES (93, 'board:update:any', '更新板块', '管理员更改板块信息', 'PUT', '/board/update', 1, '2025-05-27 22:47:04', '2025-05-27 22:47:04', 1);
INSERT INTO `permission` (`id`, `code`, `name`, `description`, `method`, `url_pattern`, `status`, `create_time`, `update_time`, `level`) VALUES (94, 'board:add:any', '添加板块', '管理员添加板块', 'POST', '/board/add', 1, '2025-05-27 22:47:04', '2025-05-27 22:47:04', 1);
INSERT INTO `permission` (`id`, `code`, `name`, `description`, `method`, `url_pattern`, `status`, `create_time`, `update_time`, `level`) VALUES (95, 'chat:sessions:own', '获取聊天列表', '用户获取所有聊天会话列表', 'GET', '/chat/sessions', 1, '2025-05-27 22:47:04', '2025-05-27 22:47:04', 3);
INSERT INTO `permission` (`id`, `code`, `name`, `description`, `method`, `url_pattern`, `status`, `create_time`, `update_time`, `level`) VALUES (96, 'chat:session-message:own', '获取聊天信息', '点击对应会话获取该会话的聊天记录', 'GET', '/chat/session/{sessionId}', 1, '2025-05-27 22:47:04', '2025-05-27 22:47:04', 3);
INSERT INTO `permission` (`id`, `code`, `name`, `description`, `method`, `url_pattern`, `status`, `create_time`, `update_time`, `level`) VALUES (97, 'chat:session-init:own', '建立聊天', '建立新的聊天会话', 'POST', '/chat/init/{targetUserId}', 1, '2025-05-27 22:47:04', '2025-05-27 22:47:04', 3);
INSERT INTO `permission` (`id`, `code`, `name`, `description`, `method`, `url_pattern`, `status`, `create_time`, `update_time`, `level`) VALUES (98, 'chat:session-delete:own', '删除聊天', '删除聊天会话', 'DELETE', '/chat/session/{sessionId}', 1, '2025-05-27 22:47:04', '2025-05-27 22:47:04', 3);
INSERT INTO `permission` (`id`, `code`, `name`, `description`, `method`, `url_pattern`, `status`, `create_time`, `update_time`, `level`) VALUES (99, 'report:submit:own', '提交举报', '用户提交举报', 'POST', '/report/submit', 1, '2025-05-27 22:47:04', '2025-05-27 22:47:04', 3);
INSERT INTO `permission` (`id`, `code`, `name`, `description`, `method`, `url_pattern`, `status`, `create_time`, `update_time`, `level`) VALUES (100, 'report:info:any', '获取所有举报信息', '管理员获取所有举报信息', 'GET', '/report/all', 1, '2025-05-27 22:47:04', '2025-05-27 22:47:04', 1);
INSERT INTO `permission` (`id`, `code`, `name`, `description`, `method`, `url_pattern`, `status`, `create_time`, `update_time`, `level`) VALUES (101, 'report:handle:any', '处理任何举报', '管理员处理任何举报', 'PUT', '/report/handle', 1, '2025-05-27 22:47:04', '2025-05-27 22:47:04', 1);
INSERT INTO `permission` (`id`, `code`, `name`, `description`, `method`, `url_pattern`, `status`, `create_time`, `update_time`, `level`) VALUES (102, 'title:list:own', '获取取得的称号列表', '用户获取取得的称号列表', 'GET', '/title/my', 1, '2025-05-27 22:47:04', '2025-05-27 22:47:04', 3);
INSERT INTO `permission` (`id`, `code`, `name`, `description`, `method`, `url_pattern`, `status`, `create_time`, `update_time`, `level`) VALUES (103, 'title:update:own', '更改展示的称号', '用户更改自己的称号', 'PUT', '/title/my/{titleId}', 1, '2025-05-27 22:47:04', '2025-05-27 22:47:04', 3);
INSERT INTO `permission` (`id`, `code`, `name`, `description`, `method`, `url_pattern`, `status`, `create_time`, `update_time`, `level`) VALUES (104, 'title:user-info:any', '获取任意用户的称号列表', '管理员查看任意用户的称号列表', 'GET', '/title/user/{userId}', 1, '2025-05-27 22:47:04', '2025-05-27 22:47:04', 1);
INSERT INTO `permission` (`id`, `code`, `name`, `description`, `method`, `url_pattern`, `status`, `create_time`, `update_time`, `level`) VALUES (105, 'title:list:any', '获取所有称号列表', '获取当前所有非内置称号列表', 'GET', '/title/list', 1, '2025-05-27 22:47:04', '2025-05-27 22:47:04', 1);
INSERT INTO `permission` (`id`, `code`, `name`, `description`, `method`, `url_pattern`, `status`, `create_time`, `update_time`, `level`) VALUES (106, 'title:update:any', '更新称号内容', '更新称号内容', 'PUT', '/title/update', 1, '2025-05-27 22:47:04', '2025-05-27 22:47:04', 1);
INSERT INTO `permission` (`id`, `code`, `name`, `description`, `method`, `url_pattern`, `status`, `create_time`, `update_time`, `level`) VALUES (107, 'title:delete:any', '删除称号', '删除非内置的称号', 'DELETE', '/title/{titleId}', 1, '2025-05-27 22:47:04', '2025-05-27 22:47:04', 1);
INSERT INTO `permission` (`id`, `code`, `name`, `description`, `method`, `url_pattern`, `status`, `create_time`, `update_time`, `level`) VALUES (108, 'title:grant:any', '授予称号', '授予任意用户称号', 'PUT', '/title/grant/{userId}/{titleId}', 1, '2025-05-27 22:47:04', '2025-05-27 22:47:04', 1);
INSERT INTO `permission` (`id`, `code`, `name`, `description`, `method`, `url_pattern`, `status`, `create_time`, `update_time`, `level`) VALUES (109, 'title:add:any', '添加称号', '添加任意的称号', 'POST', '/title/add', 1, '2025-05-27 22:47:04', '2025-05-27 22:47:04', 1);
COMMIT;

-- ----------------------------
-- Table structure for post_favorite
-- ----------------------------
DROP TABLE IF EXISTS `post_favorite`;
CREATE TABLE `post_favorite` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `user_id` bigint NOT NULL,
  `post_id` bigint NOT NULL,
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_user_post` (`user_id`,`post_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- ----------------------------
-- Records of post_favorite
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for private_chat_session
-- ----------------------------
DROP TABLE IF EXISTS `private_chat_session`;
CREATE TABLE `private_chat_session` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `user1_id` bigint NOT NULL,
  `user2_id` bigint NOT NULL,
  `user_small_id` bigint NOT NULL,
  `user_large_id` bigint NOT NULL,
  `last_active_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `uniq_user_pair` (`user_small_id`,`user_large_id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=32 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci ROW_FORMAT=DYNAMIC;

-- ----------------------------
-- Records of private_chat_session
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for private_message
-- ----------------------------
DROP TABLE IF EXISTS `private_message`;
CREATE TABLE `private_message` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `session_id` bigint NOT NULL,
  `sender_id` bigint NOT NULL,
  `receiver_id` bigint NOT NULL,
  `content` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `is_read` tinyint(1) DEFAULT '0',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`) USING BTREE,
  KEY `session_id` (`session_id`) USING BTREE,
  CONSTRAINT `private_message_ibfk_1` FOREIGN KEY (`session_id`) REFERENCES `private_chat_session` (`id`) ON DELETE CASCADE ON UPDATE RESTRICT
) ENGINE=InnoDB AUTO_INCREMENT=118 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci ROW_FORMAT=DYNAMIC;

-- ----------------------------
-- Records of private_message
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for report
-- ----------------------------
DROP TABLE IF EXISTS `report`;
CREATE TABLE `report` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '举报记录主键',
  `reporter_id` bigint NOT NULL COMMENT '举报人用户ID',
  `target_type` tinyint NOT NULL COMMENT '举报对象类型（post/comment）',
  `target_id` bigint NOT NULL COMMENT '被举报对象的ID',
  `reason` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci COMMENT '举报详细说明',
  `status` tinyint NOT NULL DEFAULT '0' COMMENT '举报状态（0待处理，1已处理，2驳回）',
  `handler_id` bigint DEFAULT NULL COMMENT '处理人ID',
  `handle_note` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci COMMENT '处理备注',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '举报时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间/处理时间',
  PRIMARY KEY (`id`) USING BTREE,
  KEY `idx_reporter` (`reporter_id`) USING BTREE,
  KEY `idx_target` (`target_type`,`target_id`) USING BTREE,
  KEY `idx_status` (`status`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=14 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci ROW_FORMAT=DYNAMIC COMMENT='用户举报信息表';

-- ----------------------------
-- Records of report
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for role
-- ----------------------------
DROP TABLE IF EXISTS `role`;
CREATE TABLE `role` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `name` varchar(50) NOT NULL COMMENT '角色编码，如 ROLE_ADMIN',
  `description` varchar(100) DEFAULT NULL COMMENT '角色描述，如 超级管理员',
  `status` tinyint DEFAULT '1',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP,
  `level` int DEFAULT '0',
  `buildin` tinyint DEFAULT '0',
  PRIMARY KEY (`id`),
  UNIQUE KEY `name` (`name`)
) ENGINE=InnoDB AUTO_INCREMENT=18 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- ----------------------------
-- Records of role
-- ----------------------------
BEGIN;
INSERT INTO `role` (`id`, `name`, `description`, `status`, `create_time`, `level`, `buildin`) VALUES (1, 'ROLE_USER', 'Normal User', 1, '2025-05-03 19:48:46', 3, 1);
INSERT INTO `role` (`id`, `name`, `description`, `status`, `create_time`, `level`, `buildin`) VALUES (2, 'ROLE_BOARD', 'Board Poster', 1, '2025-05-03 19:48:46', 2, 1);
INSERT INTO `role` (`id`, `name`, `description`, `status`, `create_time`, `level`, `buildin`) VALUES (3, 'ROLE_ADMIN', 'Administrator', 1, '2025-05-03 19:48:46', 1, 1);
INSERT INTO `role` (`id`, `name`, `description`, `status`, `create_time`, `level`, `buildin`) VALUES (4, 'ROLE_SUPER_ADMIN', 'super admin', 1, '2025-05-09 14:15:41', 0, 1);
COMMIT;

-- ----------------------------
-- Table structure for role_permission
-- ----------------------------
DROP TABLE IF EXISTS `role_permission`;
CREATE TABLE `role_permission` (
  `role_id` bigint NOT NULL,
  `permission_id` bigint NOT NULL,
  PRIMARY KEY (`role_id`,`permission_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- ----------------------------
-- Records of role_permission
-- ----------------------------
BEGIN;
INSERT INTO `role_permission` (`role_id`, `permission_id`) VALUES (1, 2);
INSERT INTO `role_permission` (`role_id`, `permission_id`) VALUES (1, 3);
INSERT INTO `role_permission` (`role_id`, `permission_id`) VALUES (1, 4);
INSERT INTO `role_permission` (`role_id`, `permission_id`) VALUES (1, 5);
INSERT INTO `role_permission` (`role_id`, `permission_id`) VALUES (1, 10);
INSERT INTO `role_permission` (`role_id`, `permission_id`) VALUES (1, 11);
INSERT INTO `role_permission` (`role_id`, `permission_id`) VALUES (1, 12);
INSERT INTO `role_permission` (`role_id`, `permission_id`) VALUES (1, 17);
INSERT INTO `role_permission` (`role_id`, `permission_id`) VALUES (1, 18);
INSERT INTO `role_permission` (`role_id`, `permission_id`) VALUES (1, 25);
INSERT INTO `role_permission` (`role_id`, `permission_id`) VALUES (1, 26);
INSERT INTO `role_permission` (`role_id`, `permission_id`) VALUES (1, 69);
INSERT INTO `role_permission` (`role_id`, `permission_id`) VALUES (1, 70);
INSERT INTO `role_permission` (`role_id`, `permission_id`) VALUES (1, 71);
INSERT INTO `role_permission` (`role_id`, `permission_id`) VALUES (1, 72);
INSERT INTO `role_permission` (`role_id`, `permission_id`) VALUES (1, 73);
INSERT INTO `role_permission` (`role_id`, `permission_id`) VALUES (1, 74);
INSERT INTO `role_permission` (`role_id`, `permission_id`) VALUES (1, 75);
INSERT INTO `role_permission` (`role_id`, `permission_id`) VALUES (1, 78);
INSERT INTO `role_permission` (`role_id`, `permission_id`) VALUES (1, 79);
INSERT INTO `role_permission` (`role_id`, `permission_id`) VALUES (1, 80);
INSERT INTO `role_permission` (`role_id`, `permission_id`) VALUES (1, 95);
INSERT INTO `role_permission` (`role_id`, `permission_id`) VALUES (1, 96);
INSERT INTO `role_permission` (`role_id`, `permission_id`) VALUES (1, 97);
INSERT INTO `role_permission` (`role_id`, `permission_id`) VALUES (1, 98);
INSERT INTO `role_permission` (`role_id`, `permission_id`) VALUES (1, 99);
INSERT INTO `role_permission` (`role_id`, `permission_id`) VALUES (1, 102);
INSERT INTO `role_permission` (`role_id`, `permission_id`) VALUES (1, 103);
INSERT INTO `role_permission` (`role_id`, `permission_id`) VALUES (2, 2);
INSERT INTO `role_permission` (`role_id`, `permission_id`) VALUES (2, 3);
INSERT INTO `role_permission` (`role_id`, `permission_id`) VALUES (2, 4);
INSERT INTO `role_permission` (`role_id`, `permission_id`) VALUES (2, 5);
INSERT INTO `role_permission` (`role_id`, `permission_id`) VALUES (2, 6);
INSERT INTO `role_permission` (`role_id`, `permission_id`) VALUES (2, 8);
INSERT INTO `role_permission` (`role_id`, `permission_id`) VALUES (2, 10);
INSERT INTO `role_permission` (`role_id`, `permission_id`) VALUES (2, 11);
INSERT INTO `role_permission` (`role_id`, `permission_id`) VALUES (2, 12);
INSERT INTO `role_permission` (`role_id`, `permission_id`) VALUES (2, 15);
INSERT INTO `role_permission` (`role_id`, `permission_id`) VALUES (2, 17);
INSERT INTO `role_permission` (`role_id`, `permission_id`) VALUES (2, 18);
INSERT INTO `role_permission` (`role_id`, `permission_id`) VALUES (2, 25);
INSERT INTO `role_permission` (`role_id`, `permission_id`) VALUES (2, 26);
INSERT INTO `role_permission` (`role_id`, `permission_id`) VALUES (2, 67);
INSERT INTO `role_permission` (`role_id`, `permission_id`) VALUES (2, 69);
INSERT INTO `role_permission` (`role_id`, `permission_id`) VALUES (2, 70);
INSERT INTO `role_permission` (`role_id`, `permission_id`) VALUES (2, 71);
INSERT INTO `role_permission` (`role_id`, `permission_id`) VALUES (2, 72);
INSERT INTO `role_permission` (`role_id`, `permission_id`) VALUES (2, 73);
INSERT INTO `role_permission` (`role_id`, `permission_id`) VALUES (2, 74);
INSERT INTO `role_permission` (`role_id`, `permission_id`) VALUES (2, 75);
INSERT INTO `role_permission` (`role_id`, `permission_id`) VALUES (2, 78);
INSERT INTO `role_permission` (`role_id`, `permission_id`) VALUES (2, 79);
INSERT INTO `role_permission` (`role_id`, `permission_id`) VALUES (2, 80);
INSERT INTO `role_permission` (`role_id`, `permission_id`) VALUES (2, 95);
INSERT INTO `role_permission` (`role_id`, `permission_id`) VALUES (2, 96);
INSERT INTO `role_permission` (`role_id`, `permission_id`) VALUES (2, 97);
INSERT INTO `role_permission` (`role_id`, `permission_id`) VALUES (2, 98);
INSERT INTO `role_permission` (`role_id`, `permission_id`) VALUES (2, 99);
INSERT INTO `role_permission` (`role_id`, `permission_id`) VALUES (2, 102);
INSERT INTO `role_permission` (`role_id`, `permission_id`) VALUES (2, 103);
INSERT INTO `role_permission` (`role_id`, `permission_id`) VALUES (3, 1);
INSERT INTO `role_permission` (`role_id`, `permission_id`) VALUES (3, 2);
INSERT INTO `role_permission` (`role_id`, `permission_id`) VALUES (3, 3);
INSERT INTO `role_permission` (`role_id`, `permission_id`) VALUES (3, 4);
INSERT INTO `role_permission` (`role_id`, `permission_id`) VALUES (3, 5);
INSERT INTO `role_permission` (`role_id`, `permission_id`) VALUES (3, 6);
INSERT INTO `role_permission` (`role_id`, `permission_id`) VALUES (3, 7);
INSERT INTO `role_permission` (`role_id`, `permission_id`) VALUES (3, 8);
INSERT INTO `role_permission` (`role_id`, `permission_id`) VALUES (3, 9);
INSERT INTO `role_permission` (`role_id`, `permission_id`) VALUES (3, 10);
INSERT INTO `role_permission` (`role_id`, `permission_id`) VALUES (3, 11);
INSERT INTO `role_permission` (`role_id`, `permission_id`) VALUES (3, 12);
INSERT INTO `role_permission` (`role_id`, `permission_id`) VALUES (3, 15);
INSERT INTO `role_permission` (`role_id`, `permission_id`) VALUES (3, 16);
INSERT INTO `role_permission` (`role_id`, `permission_id`) VALUES (3, 17);
INSERT INTO `role_permission` (`role_id`, `permission_id`) VALUES (3, 18);
INSERT INTO `role_permission` (`role_id`, `permission_id`) VALUES (3, 19);
INSERT INTO `role_permission` (`role_id`, `permission_id`) VALUES (3, 20);
INSERT INTO `role_permission` (`role_id`, `permission_id`) VALUES (3, 21);
INSERT INTO `role_permission` (`role_id`, `permission_id`) VALUES (3, 22);
INSERT INTO `role_permission` (`role_id`, `permission_id`) VALUES (3, 23);
INSERT INTO `role_permission` (`role_id`, `permission_id`) VALUES (3, 25);
INSERT INTO `role_permission` (`role_id`, `permission_id`) VALUES (3, 26);
INSERT INTO `role_permission` (`role_id`, `permission_id`) VALUES (3, 67);
INSERT INTO `role_permission` (`role_id`, `permission_id`) VALUES (3, 68);
INSERT INTO `role_permission` (`role_id`, `permission_id`) VALUES (3, 69);
INSERT INTO `role_permission` (`role_id`, `permission_id`) VALUES (3, 70);
INSERT INTO `role_permission` (`role_id`, `permission_id`) VALUES (3, 71);
INSERT INTO `role_permission` (`role_id`, `permission_id`) VALUES (3, 72);
INSERT INTO `role_permission` (`role_id`, `permission_id`) VALUES (3, 73);
INSERT INTO `role_permission` (`role_id`, `permission_id`) VALUES (3, 74);
INSERT INTO `role_permission` (`role_id`, `permission_id`) VALUES (3, 75);
INSERT INTO `role_permission` (`role_id`, `permission_id`) VALUES (3, 76);
INSERT INTO `role_permission` (`role_id`, `permission_id`) VALUES (3, 77);
INSERT INTO `role_permission` (`role_id`, `permission_id`) VALUES (3, 78);
INSERT INTO `role_permission` (`role_id`, `permission_id`) VALUES (3, 79);
INSERT INTO `role_permission` (`role_id`, `permission_id`) VALUES (3, 80);
INSERT INTO `role_permission` (`role_id`, `permission_id`) VALUES (3, 81);
INSERT INTO `role_permission` (`role_id`, `permission_id`) VALUES (3, 82);
INSERT INTO `role_permission` (`role_id`, `permission_id`) VALUES (3, 83);
INSERT INTO `role_permission` (`role_id`, `permission_id`) VALUES (3, 84);
INSERT INTO `role_permission` (`role_id`, `permission_id`) VALUES (3, 85);
INSERT INTO `role_permission` (`role_id`, `permission_id`) VALUES (3, 86);
INSERT INTO `role_permission` (`role_id`, `permission_id`) VALUES (3, 87);
INSERT INTO `role_permission` (`role_id`, `permission_id`) VALUES (3, 88);
INSERT INTO `role_permission` (`role_id`, `permission_id`) VALUES (3, 92);
INSERT INTO `role_permission` (`role_id`, `permission_id`) VALUES (3, 93);
INSERT INTO `role_permission` (`role_id`, `permission_id`) VALUES (3, 94);
INSERT INTO `role_permission` (`role_id`, `permission_id`) VALUES (3, 95);
INSERT INTO `role_permission` (`role_id`, `permission_id`) VALUES (3, 96);
INSERT INTO `role_permission` (`role_id`, `permission_id`) VALUES (3, 97);
INSERT INTO `role_permission` (`role_id`, `permission_id`) VALUES (3, 98);
INSERT INTO `role_permission` (`role_id`, `permission_id`) VALUES (3, 99);
INSERT INTO `role_permission` (`role_id`, `permission_id`) VALUES (3, 100);
INSERT INTO `role_permission` (`role_id`, `permission_id`) VALUES (3, 101);
INSERT INTO `role_permission` (`role_id`, `permission_id`) VALUES (3, 102);
INSERT INTO `role_permission` (`role_id`, `permission_id`) VALUES (3, 103);
INSERT INTO `role_permission` (`role_id`, `permission_id`) VALUES (3, 104);
INSERT INTO `role_permission` (`role_id`, `permission_id`) VALUES (3, 105);
INSERT INTO `role_permission` (`role_id`, `permission_id`) VALUES (3, 106);
INSERT INTO `role_permission` (`role_id`, `permission_id`) VALUES (3, 107);
INSERT INTO `role_permission` (`role_id`, `permission_id`) VALUES (3, 108);
INSERT INTO `role_permission` (`role_id`, `permission_id`) VALUES (3, 109);
INSERT INTO `role_permission` (`role_id`, `permission_id`) VALUES (4, 1);
INSERT INTO `role_permission` (`role_id`, `permission_id`) VALUES (4, 2);
INSERT INTO `role_permission` (`role_id`, `permission_id`) VALUES (4, 3);
INSERT INTO `role_permission` (`role_id`, `permission_id`) VALUES (4, 4);
INSERT INTO `role_permission` (`role_id`, `permission_id`) VALUES (4, 5);
INSERT INTO `role_permission` (`role_id`, `permission_id`) VALUES (4, 6);
INSERT INTO `role_permission` (`role_id`, `permission_id`) VALUES (4, 7);
INSERT INTO `role_permission` (`role_id`, `permission_id`) VALUES (4, 8);
INSERT INTO `role_permission` (`role_id`, `permission_id`) VALUES (4, 9);
INSERT INTO `role_permission` (`role_id`, `permission_id`) VALUES (4, 10);
INSERT INTO `role_permission` (`role_id`, `permission_id`) VALUES (4, 11);
INSERT INTO `role_permission` (`role_id`, `permission_id`) VALUES (4, 12);
INSERT INTO `role_permission` (`role_id`, `permission_id`) VALUES (4, 15);
INSERT INTO `role_permission` (`role_id`, `permission_id`) VALUES (4, 16);
INSERT INTO `role_permission` (`role_id`, `permission_id`) VALUES (4, 17);
INSERT INTO `role_permission` (`role_id`, `permission_id`) VALUES (4, 18);
INSERT INTO `role_permission` (`role_id`, `permission_id`) VALUES (4, 19);
INSERT INTO `role_permission` (`role_id`, `permission_id`) VALUES (4, 20);
INSERT INTO `role_permission` (`role_id`, `permission_id`) VALUES (4, 21);
INSERT INTO `role_permission` (`role_id`, `permission_id`) VALUES (4, 22);
INSERT INTO `role_permission` (`role_id`, `permission_id`) VALUES (4, 23);
INSERT INTO `role_permission` (`role_id`, `permission_id`) VALUES (4, 25);
INSERT INTO `role_permission` (`role_id`, `permission_id`) VALUES (4, 26);
INSERT INTO `role_permission` (`role_id`, `permission_id`) VALUES (4, 67);
INSERT INTO `role_permission` (`role_id`, `permission_id`) VALUES (4, 68);
INSERT INTO `role_permission` (`role_id`, `permission_id`) VALUES (4, 69);
INSERT INTO `role_permission` (`role_id`, `permission_id`) VALUES (4, 70);
INSERT INTO `role_permission` (`role_id`, `permission_id`) VALUES (4, 71);
INSERT INTO `role_permission` (`role_id`, `permission_id`) VALUES (4, 72);
INSERT INTO `role_permission` (`role_id`, `permission_id`) VALUES (4, 73);
INSERT INTO `role_permission` (`role_id`, `permission_id`) VALUES (4, 74);
INSERT INTO `role_permission` (`role_id`, `permission_id`) VALUES (4, 75);
INSERT INTO `role_permission` (`role_id`, `permission_id`) VALUES (4, 76);
INSERT INTO `role_permission` (`role_id`, `permission_id`) VALUES (4, 77);
INSERT INTO `role_permission` (`role_id`, `permission_id`) VALUES (4, 78);
INSERT INTO `role_permission` (`role_id`, `permission_id`) VALUES (4, 79);
INSERT INTO `role_permission` (`role_id`, `permission_id`) VALUES (4, 80);
INSERT INTO `role_permission` (`role_id`, `permission_id`) VALUES (4, 81);
INSERT INTO `role_permission` (`role_id`, `permission_id`) VALUES (4, 82);
INSERT INTO `role_permission` (`role_id`, `permission_id`) VALUES (4, 83);
INSERT INTO `role_permission` (`role_id`, `permission_id`) VALUES (4, 84);
INSERT INTO `role_permission` (`role_id`, `permission_id`) VALUES (4, 85);
INSERT INTO `role_permission` (`role_id`, `permission_id`) VALUES (4, 86);
INSERT INTO `role_permission` (`role_id`, `permission_id`) VALUES (4, 87);
INSERT INTO `role_permission` (`role_id`, `permission_id`) VALUES (4, 88);
INSERT INTO `role_permission` (`role_id`, `permission_id`) VALUES (4, 89);
INSERT INTO `role_permission` (`role_id`, `permission_id`) VALUES (4, 90);
INSERT INTO `role_permission` (`role_id`, `permission_id`) VALUES (4, 91);
INSERT INTO `role_permission` (`role_id`, `permission_id`) VALUES (4, 92);
INSERT INTO `role_permission` (`role_id`, `permission_id`) VALUES (4, 93);
INSERT INTO `role_permission` (`role_id`, `permission_id`) VALUES (4, 94);
INSERT INTO `role_permission` (`role_id`, `permission_id`) VALUES (4, 95);
INSERT INTO `role_permission` (`role_id`, `permission_id`) VALUES (4, 96);
INSERT INTO `role_permission` (`role_id`, `permission_id`) VALUES (4, 97);
INSERT INTO `role_permission` (`role_id`, `permission_id`) VALUES (4, 98);
INSERT INTO `role_permission` (`role_id`, `permission_id`) VALUES (4, 99);
INSERT INTO `role_permission` (`role_id`, `permission_id`) VALUES (4, 100);
INSERT INTO `role_permission` (`role_id`, `permission_id`) VALUES (4, 101);
INSERT INTO `role_permission` (`role_id`, `permission_id`) VALUES (4, 102);
INSERT INTO `role_permission` (`role_id`, `permission_id`) VALUES (4, 103);
INSERT INTO `role_permission` (`role_id`, `permission_id`) VALUES (4, 104);
INSERT INTO `role_permission` (`role_id`, `permission_id`) VALUES (4, 105);
INSERT INTO `role_permission` (`role_id`, `permission_id`) VALUES (4, 106);
INSERT INTO `role_permission` (`role_id`, `permission_id`) VALUES (4, 107);
INSERT INTO `role_permission` (`role_id`, `permission_id`) VALUES (4, 108);
INSERT INTO `role_permission` (`role_id`, `permission_id`) VALUES (4, 109);
COMMIT;

-- ----------------------------
-- Table structure for title
-- ----------------------------
DROP TABLE IF EXISTS `title`;
CREATE TABLE `title` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `name` varchar(100) NOT NULL,
  `description` text,
  `condition_type` varchar(50) DEFAULT NULL,
  `condition_value` int DEFAULT NULL,
  `icon_url` varchar(200) DEFAULT NULL,
  `buildin` tinyint NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=14 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- ----------------------------
-- Records of title
-- ----------------------------
BEGIN;
INSERT INTO `title` (`id`, `name`, `description`, `condition_type`, `condition_value`, `icon_url`, `buildin`) VALUES (1, 'Greenhorn', 'A newcomer who just started the journey.', 'exp', 0, NULL, 1);
INSERT INTO `title` (`id`, `name`, `description`, `condition_type`, `condition_value`, `icon_url`, `buildin`) VALUES (2, 'Explorer', 'Reached 100 EXP. Starting to explore the world.', 'exp', 100, NULL, 1);
INSERT INTO `title` (`id`, `name`, `description`, `condition_type`, `condition_value`, `icon_url`, `buildin`) VALUES (3, 'Trailblazer', 'Reached 300 EXP. Gaining momentum.', 'exp', 300, NULL, 1);
INSERT INTO `title` (`id`, `name`, `description`, `condition_type`, `condition_value`, `icon_url`, `buildin`) VALUES (4, 'Veteran', 'Reached 700 EXP. Experienced and steady.', 'exp', 700, NULL, 1);
INSERT INTO `title` (`id`, `name`, `description`, `condition_type`, `condition_value`, `icon_url`, `buildin`) VALUES (5, 'Expert', 'Reached 1500 EXP. Highly skilled.', 'exp', 1500, NULL, 1);
INSERT INTO `title` (`id`, `name`, `description`, `condition_type`, `condition_value`, `icon_url`, `buildin`) VALUES (6, 'Master', 'Reached 3000 EXP. Master of the field.', 'exp', 3000, NULL, 1);
INSERT INTO `title` (`id`, `name`, `description`, `condition_type`, `condition_value`, `icon_url`, `buildin`) VALUES (7, 'Legend', 'Reached 5000 EXP. A legendary existence.', 'exp', 5000, NULL, 1);
COMMIT;

-- ----------------------------
-- Table structure for user
-- ----------------------------
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user` (
  `id` int NOT NULL AUTO_INCREMENT,
  `username` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `password` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
  `email` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `type` int DEFAULT NULL COMMENT '0-普通用户；1-超级管理员；2-版主；',
  `status` int DEFAULT NULL COMMENT '0-未激活；1-已激活；',
  `header_url` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `bio` mediumtext CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci,
  `nickname` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '用户昵称',
  `last_username_update_time` timestamp NULL DEFAULT NULL COMMENT '最后一次修改用户名时间',
  `last_email_update_time` timestamp NULL DEFAULT NULL COMMENT '最后一次修改邮箱时间',
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `exp_points` int NOT NULL DEFAULT '0',
  `title_id` bigint DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uniq_username` (`username`),
  UNIQUE KEY `uniq_email` (`email`),
  KEY `index_username` (`username`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=27 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- ----------------------------
-- Records of user
-- ----------------------------
BEGIN;
INSERT INTO `user` (`id`, `username`, `password`, `email`, `type`, `status`, `header_url`, `create_time`, `bio`, `nickname`, `last_username_update_time`, `last_email_update_time`, `update_time`, `exp_points`, `title_id`) VALUES (7, 'AdminUser', '$2a$10$edAAzJ07MRa7xyJ8PSMY8uaEajSVAfQr.Aik3JUp1mIUI/Afavto6', 'gabrillo1774@gmail.com', 0, 1, 'https://api.dicebear.com/7.x/avataaars/svg?seed=avatar1', '2025-05-14 23:15:17', '哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈', 'admin_user', '2025-05-10 23:14:35', NULL, '2025-05-21 18:35:48', 6, 1);
INSERT INTO `user` (`id`, `username`, `password`, `email`, `type`, `status`, `header_url`, `create_time`, `bio`, `nickname`, `last_username_update_time`, `last_email_update_time`, `update_time`, `exp_points`, `title_id`) VALUES (18, 'NormalUser', '$2a$10$edAAzJ07MRa7xyJ8PSMY8uaEajSVAfQr.Aik3JUp1mIUI/Afavto6', 'gabrillo177dd@gmail.com', 0, 1, 'https://api.dicebear.com/7.x/avataaars/svg?seed=avatar63', '2025-05-20 19:43:09', '', 'normaluser', '2025-05-10 23:16:18', NULL, '2025-05-22 20:36:57', 19, 1);
INSERT INTO `user` (`id`, `username`, `password`, `email`, `type`, `status`, `header_url`, `create_time`, `bio`, `nickname`, `last_username_update_time`, `last_email_update_time`, `update_time`, `exp_points`, `title_id`) VALUES (19, 'SuperAdmin', '$2a$10$Dl/weYLzxRb3nPT2NQ2mxugT8ty2Aitc4IBQILP3GWl1CXNwjqsHq', 'gabrillo177444@gmail.com', 0, 1, 'https://api.dicebear.com/7.x/avataaars/svg?seed=avatar63', '2025-05-12 13:03:00', '', 'superadmin', '2025-05-10 22:53:42', NULL, '2025-05-26 02:07:06', 85, 2);
INSERT INTO `user` (`id`, `username`, `password`, `email`, `type`, `status`, `header_url`, `create_time`, `bio`, `nickname`, `last_username_update_time`, `last_email_update_time`, `update_time`, `exp_points`, `title_id`) VALUES (23, 'testUser', '$2a$10$NRA14cegigh.6tU1ckTxFezK2HSlEYf/oYJrrOdtP7FURFo5DpEI.', 'gabrillo177@gmail.com', NULL, 1, 'https://api.dicebear.com/7.x/avataaars/svg?seed=avatar1', '2025-05-23 02:28:02', NULL, 'testUser', NULL, NULL, '2025-05-25 01:25:16', 0, 1);
INSERT INTO `user` (`id`, `username`, `password`, `email`, `type`, `status`, `header_url`, `create_time`, `bio`, `nickname`, `last_username_update_time`, `last_email_update_time`, `update_time`, `exp_points`, `title_id`) VALUES (24, 'tentanus', '$2a$10$CUYghmpc4tUZSkQe20cuxuplH3WeYdKwBkUkKHmJ0ZgpjT/dTSW1.', 'tentanusw@gmail.com', NULL, 1, 'https://api.dicebear.com/7.x/avataaars/svg?seed=avatar1', '2025-05-23 13:03:44', NULL, 'tentanus', NULL, NULL, '2025-05-23 14:34:30', 11, 1);
INSERT INTO `user` (`id`, `username`, `password`, `email`, `type`, `status`, `header_url`, `create_time`, `bio`, `nickname`, `last_username_update_time`, `last_email_update_time`, `update_time`, `exp_points`, `title_id`) VALUES (25, '169419', '$2a$10$SdegA9o5bdKzsNwLL655zOCrbESjmoFJh1Q1AhzcE6fkanPAw82XC', 'pacoyunodos@gmail.com', NULL, 1, 'https://api.dicebear.com/7.x/avataaars/svg?seed=avatar1', '2025-05-25 01:20:51', NULL, '169419', NULL, NULL, '2025-05-25 01:30:42', 5, 1);
INSERT INTO `user` (`id`, `username`, `password`, `email`, `type`, `status`, `header_url`, `create_time`, `bio`, `nickname`, `last_username_update_time`, `last_email_update_time`, `update_time`, `exp_points`, `title_id`) VALUES (26, 'Psyche0920', '$2a$10$5h4QpwOkztdQWywqPYwBKuCJp02N38CYg03ONWZnKREgZFFkd5lAe', 'hanweijia.ann@outlook.com', NULL, 1, 'https://api.dicebear.com/7.x/avataaars/svg?seed=avatar1', '2025-05-25 14:04:39', NULL, 'Psyche0920', NULL, NULL, '2025-05-25 14:05:28', 0, 1);
COMMIT;

-- ----------------------------
-- Table structure for user_activity_stats
-- ----------------------------
DROP TABLE IF EXISTS `user_activity_stats`;
CREATE TABLE `user_activity_stats` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `stat_date` date DEFAULT NULL,
  `stat_month` varchar(7) DEFAULT NULL,
  `active_user_count` int NOT NULL,
  `type` enum('DAY','MONTH') NOT NULL,
  `created_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- ----------------------------
-- Records of user_activity_stats
-- ----------------------------
BEGIN;
INSERT INTO `user_activity_stats` (`id`, `stat_date`, `stat_month`, `active_user_count`, `type`, `created_time`) VALUES (1, '2025-05-20', NULL, 3, 'DAY', '2025-05-20 23:59:00');
INSERT INTO `user_activity_stats` (`id`, `stat_date`, `stat_month`, `active_user_count`, `type`, `created_time`) VALUES (2, '2025-05-21', NULL, 3, 'DAY', '2025-05-21 23:59:00');
INSERT INTO `user_activity_stats` (`id`, `stat_date`, `stat_month`, `active_user_count`, `type`, `created_time`) VALUES (3, '2025-05-22', NULL, 1, 'DAY', '2025-05-23 01:59:00');
INSERT INTO `user_activity_stats` (`id`, `stat_date`, `stat_month`, `active_user_count`, `type`, `created_time`) VALUES (4, '2025-05-23', NULL, 3, 'DAY', '2025-05-24 01:59:00');
INSERT INTO `user_activity_stats` (`id`, `stat_date`, `stat_month`, `active_user_count`, `type`, `created_time`) VALUES (5, '2025-05-24', NULL, 3, 'DAY', '2025-05-25 01:59:00');
INSERT INTO `user_activity_stats` (`id`, `stat_date`, `stat_month`, `active_user_count`, `type`, `created_time`) VALUES (6, '2025-05-25', NULL, 3, 'DAY', '2025-05-26 01:59:00');
INSERT INTO `user_activity_stats` (`id`, `stat_date`, `stat_month`, `active_user_count`, `type`, `created_time`) VALUES (7, '2025-05-26', NULL, 1, 'DAY', '2025-05-27 01:59:00');
COMMIT;

-- ----------------------------
-- Table structure for user_chat_session
-- ----------------------------
DROP TABLE IF EXISTS `user_chat_session`;
CREATE TABLE `user_chat_session` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `user_id` bigint NOT NULL,
  `session_id` bigint NOT NULL,
  `other_user_id` bigint NOT NULL,
  `last_active_time` datetime NOT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `uniq_user_session` (`user_id`,`session_id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=71 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci ROW_FORMAT=DYNAMIC;

-- ----------------------------
-- Records of user_chat_session
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for user_exp_log
-- ----------------------------
DROP TABLE IF EXISTS `user_exp_log`;
CREATE TABLE `user_exp_log` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `user_id` bigint NOT NULL,
  `change_value` int NOT NULL,
  `reason` varchar(100) NOT NULL,
  `related_type` int DEFAULT NULL,
  `related_id` bigint DEFAULT NULL,
  `create_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=134 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- ----------------------------
-- Records of user_exp_log
-- ----------------------------
BEGIN;
INSERT INTO `user_exp_log` (`id`, `user_id`, `change_value`, `reason`, `related_type`, `related_id`, `create_time`) VALUES (61, 19, 3, '发布帖子', 1, 67, NULL);
INSERT INTO `user_exp_log` (`id`, `user_id`, `change_value`, `reason`, `related_type`, `related_id`, `create_time`) VALUES (62, 19, 1, '被点赞帖子', 1, 67, NULL);
INSERT INTO `user_exp_log` (`id`, `user_id`, `change_value`, `reason`, `related_type`, `related_id`, `create_time`) VALUES (63, 19, 1, '发布评论', 2, 268, NULL);
INSERT INTO `user_exp_log` (`id`, `user_id`, `change_value`, `reason`, `related_type`, `related_id`, `create_time`) VALUES (64, 19, 1, '发布评论', 2, 269, NULL);
INSERT INTO `user_exp_log` (`id`, `user_id`, `change_value`, `reason`, `related_type`, `related_id`, `create_time`) VALUES (65, 19, 1, '发布评论', 2, 270, NULL);
INSERT INTO `user_exp_log` (`id`, `user_id`, `change_value`, `reason`, `related_type`, `related_id`, `create_time`) VALUES (66, 24, 3, '发布帖子', 1, 68, NULL);
INSERT INTO `user_exp_log` (`id`, `user_id`, `change_value`, `reason`, `related_type`, `related_id`, `create_time`) VALUES (67, 24, 1, '被点赞帖子', 1, 68, NULL);
INSERT INTO `user_exp_log` (`id`, `user_id`, `change_value`, `reason`, `related_type`, `related_id`, `create_time`) VALUES (68, 19, 1, '发布评论', 2, 271, NULL);
INSERT INTO `user_exp_log` (`id`, `user_id`, `change_value`, `reason`, `related_type`, `related_id`, `create_time`) VALUES (69, 24, 3, '发布帖子', 1, 69, NULL);
INSERT INTO `user_exp_log` (`id`, `user_id`, `change_value`, `reason`, `related_type`, `related_id`, `create_time`) VALUES (70, 19, 1, '发布评论', 2, 272, NULL);
INSERT INTO `user_exp_log` (`id`, `user_id`, `change_value`, `reason`, `related_type`, `related_id`, `create_time`) VALUES (71, 19, 1, '发布评论', 2, 273, NULL);
INSERT INTO `user_exp_log` (`id`, `user_id`, `change_value`, `reason`, `related_type`, `related_id`, `create_time`) VALUES (72, 24, 1, '被点赞帖子', 1, 69, NULL);
INSERT INTO `user_exp_log` (`id`, `user_id`, `change_value`, `reason`, `related_type`, `related_id`, `create_time`) VALUES (73, 19, 1, '发布评论', 2, 274, NULL);
INSERT INTO `user_exp_log` (`id`, `user_id`, `change_value`, `reason`, `related_type`, `related_id`, `create_time`) VALUES (74, 19, 1, '发布评论', 2, 275, NULL);
INSERT INTO `user_exp_log` (`id`, `user_id`, `change_value`, `reason`, `related_type`, `related_id`, `create_time`) VALUES (75, 19, 1, '发布评论', 2, 276, NULL);
INSERT INTO `user_exp_log` (`id`, `user_id`, `change_value`, `reason`, `related_type`, `related_id`, `create_time`) VALUES (76, 19, -1, '删除评论', 2, 276, NULL);
INSERT INTO `user_exp_log` (`id`, `user_id`, `change_value`, `reason`, `related_type`, `related_id`, `create_time`) VALUES (77, 19, 1, '发布评论', 2, 277, NULL);
INSERT INTO `user_exp_log` (`id`, `user_id`, `change_value`, `reason`, `related_type`, `related_id`, `create_time`) VALUES (78, 24, 1, '被点赞帖子', 1, 69, NULL);
INSERT INTO `user_exp_log` (`id`, `user_id`, `change_value`, `reason`, `related_type`, `related_id`, `create_time`) VALUES (79, 19, 1, '被点赞评论', 2, 273, NULL);
INSERT INTO `user_exp_log` (`id`, `user_id`, `change_value`, `reason`, `related_type`, `related_id`, `create_time`) VALUES (80, 19, 1, '被点赞评论', 2, 274, NULL);
INSERT INTO `user_exp_log` (`id`, `user_id`, `change_value`, `reason`, `related_type`, `related_id`, `create_time`) VALUES (81, 19, 1, '发布评论', 2, 278, NULL);
INSERT INTO `user_exp_log` (`id`, `user_id`, `change_value`, `reason`, `related_type`, `related_id`, `create_time`) VALUES (82, 19, 1, '被点赞评论', 2, 273, NULL);
INSERT INTO `user_exp_log` (`id`, `user_id`, `change_value`, `reason`, `related_type`, `related_id`, `create_time`) VALUES (83, 24, 1, '发布评论', 2, 279, NULL);
INSERT INTO `user_exp_log` (`id`, `user_id`, `change_value`, `reason`, `related_type`, `related_id`, `create_time`) VALUES (84, 19, 1, '被点赞评论', 2, 275, NULL);
INSERT INTO `user_exp_log` (`id`, `user_id`, `change_value`, `reason`, `related_type`, `related_id`, `create_time`) VALUES (85, 19, 1, '被点赞评论', 2, 277, NULL);
INSERT INTO `user_exp_log` (`id`, `user_id`, `change_value`, `reason`, `related_type`, `related_id`, `create_time`) VALUES (86, 24, -1, '删除评论', 2, 279, NULL);
INSERT INTO `user_exp_log` (`id`, `user_id`, `change_value`, `reason`, `related_type`, `related_id`, `create_time`) VALUES (87, 24, 1, '发布评论', 2, 280, NULL);
INSERT INTO `user_exp_log` (`id`, `user_id`, `change_value`, `reason`, `related_type`, `related_id`, `create_time`) VALUES (88, 19, 1, '发布评论', 2, 281, NULL);
INSERT INTO `user_exp_log` (`id`, `user_id`, `change_value`, `reason`, `related_type`, `related_id`, `create_time`) VALUES (89, 19, 1, '被点赞评论', 2, 278, NULL);
INSERT INTO `user_exp_log` (`id`, `user_id`, `change_value`, `reason`, `related_type`, `related_id`, `create_time`) VALUES (90, 19, -1, '被取消点赞评论', 2, 278, NULL);
INSERT INTO `user_exp_log` (`id`, `user_id`, `change_value`, `reason`, `related_type`, `related_id`, `create_time`) VALUES (91, 24, 1, '被点赞评论', 2, 280, NULL);
INSERT INTO `user_exp_log` (`id`, `user_id`, `change_value`, `reason`, `related_type`, `related_id`, `create_time`) VALUES (92, 19, 1, '发布评论', 2, 282, NULL);
INSERT INTO `user_exp_log` (`id`, `user_id`, `change_value`, `reason`, `related_type`, `related_id`, `create_time`) VALUES (93, 19, 3, '发布帖子', 1, 70, NULL);
INSERT INTO `user_exp_log` (`id`, `user_id`, `change_value`, `reason`, `related_type`, `related_id`, `create_time`) VALUES (94, 19, 1, '发布评论', 2, 283, NULL);
INSERT INTO `user_exp_log` (`id`, `user_id`, `change_value`, `reason`, `related_type`, `related_id`, `create_time`) VALUES (95, 19, 1, '被点赞帖子', 1, 70, NULL);
INSERT INTO `user_exp_log` (`id`, `user_id`, `change_value`, `reason`, `related_type`, `related_id`, `create_time`) VALUES (96, 19, -1, '被取消点赞帖子', 1, 70, NULL);
INSERT INTO `user_exp_log` (`id`, `user_id`, `change_value`, `reason`, `related_type`, `related_id`, `create_time`) VALUES (97, 19, 1, '发布评论', 2, 284, NULL);
INSERT INTO `user_exp_log` (`id`, `user_id`, `change_value`, `reason`, `related_type`, `related_id`, `create_time`) VALUES (98, 19, -1, '删除评论', 2, 284, NULL);
INSERT INTO `user_exp_log` (`id`, `user_id`, `change_value`, `reason`, `related_type`, `related_id`, `create_time`) VALUES (99, 19, 1, '被点赞帖子', 1, 70, NULL);
INSERT INTO `user_exp_log` (`id`, `user_id`, `change_value`, `reason`, `related_type`, `related_id`, `create_time`) VALUES (100, 25, 3, '发布帖子', 1, 71, NULL);
INSERT INTO `user_exp_log` (`id`, `user_id`, `change_value`, `reason`, `related_type`, `related_id`, `create_time`) VALUES (101, 23, 1, '发布评论', 2, 285, NULL);
INSERT INTO `user_exp_log` (`id`, `user_id`, `change_value`, `reason`, `related_type`, `related_id`, `create_time`) VALUES (102, 23, -1, '删除评论', 2, 285, NULL);
INSERT INTO `user_exp_log` (`id`, `user_id`, `change_value`, `reason`, `related_type`, `related_id`, `create_time`) VALUES (103, 25, 3, '发布帖子', 1, 72, NULL);
INSERT INTO `user_exp_log` (`id`, `user_id`, `change_value`, `reason`, `related_type`, `related_id`, `create_time`) VALUES (104, 25, 1, '被点赞帖子', 1, 72, NULL);
INSERT INTO `user_exp_log` (`id`, `user_id`, `change_value`, `reason`, `related_type`, `related_id`, `create_time`) VALUES (105, 25, 1, '被点赞帖子', 1, 71, NULL);
INSERT INTO `user_exp_log` (`id`, `user_id`, `change_value`, `reason`, `related_type`, `related_id`, `create_time`) VALUES (106, 25, -1, '被取消点赞帖子', 1, 72, NULL);
INSERT INTO `user_exp_log` (`id`, `user_id`, `change_value`, `reason`, `related_type`, `related_id`, `create_time`) VALUES (107, 25, -1, '被取消点赞帖子', 1, 71, NULL);
INSERT INTO `user_exp_log` (`id`, `user_id`, `change_value`, `reason`, `related_type`, `related_id`, `create_time`) VALUES (108, 25, 1, '被点赞帖子', 1, 72, NULL);
INSERT INTO `user_exp_log` (`id`, `user_id`, `change_value`, `reason`, `related_type`, `related_id`, `create_time`) VALUES (109, 25, 1, '被点赞帖子', 1, 72, NULL);
INSERT INTO `user_exp_log` (`id`, `user_id`, `change_value`, `reason`, `related_type`, `related_id`, `create_time`) VALUES (110, 25, -3, '删除帖子', 1, 72, NULL);
INSERT INTO `user_exp_log` (`id`, `user_id`, `change_value`, `reason`, `related_type`, `related_id`, `create_time`) VALUES (111, 19, 1, '被点赞评论', 2, 283, NULL);
INSERT INTO `user_exp_log` (`id`, `user_id`, `change_value`, `reason`, `related_type`, `related_id`, `create_time`) VALUES (112, 19, 3, '发布帖子', 1, 73, NULL);
INSERT INTO `user_exp_log` (`id`, `user_id`, `change_value`, `reason`, `related_type`, `related_id`, `create_time`) VALUES (113, 19, 1, '发布评论', 2, 286, NULL);
INSERT INTO `user_exp_log` (`id`, `user_id`, `change_value`, `reason`, `related_type`, `related_id`, `create_time`) VALUES (114, 19, -1, '删除评论', 2, 286, NULL);
COMMIT;

-- ----------------------------
-- Table structure for user_permission_override
-- ----------------------------
DROP TABLE IF EXISTS `user_permission_override`;
CREATE TABLE `user_permission_override` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `user_id` bigint NOT NULL,
  `permission_id` bigint NOT NULL,
  `is_allowed` tinyint(1) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `user_id` (`user_id`,`permission_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- ----------------------------
-- Records of user_permission_override
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for user_role
-- ----------------------------
DROP TABLE IF EXISTS `user_role`;
CREATE TABLE `user_role` (
  `user_id` bigint NOT NULL,
  `role_id` bigint NOT NULL,
  PRIMARY KEY (`user_id`,`role_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- ----------------------------
-- Records of user_role
-- ----------------------------
BEGIN;
INSERT INTO `user_role` (`user_id`, `role_id`) VALUES (1, 1);
INSERT INTO `user_role` (`user_id`, `role_id`) VALUES (1, 2);
INSERT INTO `user_role` (`user_id`, `role_id`) VALUES (1, 3);
INSERT INTO `user_role` (`user_id`, `role_id`) VALUES (2, 1);
INSERT INTO `user_role` (`user_id`, `role_id`) VALUES (2, 2);
INSERT INTO `user_role` (`user_id`, `role_id`) VALUES (3, 1);
INSERT INTO `user_role` (`user_id`, `role_id`) VALUES (4, 1);
INSERT INTO `user_role` (`user_id`, `role_id`) VALUES (4, 3);
INSERT INTO `user_role` (`user_id`, `role_id`) VALUES (5, 1);
INSERT INTO `user_role` (`user_id`, `role_id`) VALUES (5, 2);
INSERT INTO `user_role` (`user_id`, `role_id`) VALUES (7, 3);
INSERT INTO `user_role` (`user_id`, `role_id`) VALUES (8, 1);
INSERT INTO `user_role` (`user_id`, `role_id`) VALUES (8, 2);
INSERT INTO `user_role` (`user_id`, `role_id`) VALUES (9, 2);
INSERT INTO `user_role` (`user_id`, `role_id`) VALUES (10, 1);
INSERT INTO `user_role` (`user_id`, `role_id`) VALUES (11, 1);
INSERT INTO `user_role` (`user_id`, `role_id`) VALUES (12, 1);
INSERT INTO `user_role` (`user_id`, `role_id`) VALUES (13, 1);
INSERT INTO `user_role` (`user_id`, `role_id`) VALUES (14, 1);
INSERT INTO `user_role` (`user_id`, `role_id`) VALUES (15, 1);
INSERT INTO `user_role` (`user_id`, `role_id`) VALUES (15, 3);
INSERT INTO `user_role` (`user_id`, `role_id`) VALUES (16, 1);
INSERT INTO `user_role` (`user_id`, `role_id`) VALUES (17, 1);
INSERT INTO `user_role` (`user_id`, `role_id`) VALUES (18, 1);
INSERT INTO `user_role` (`user_id`, `role_id`) VALUES (19, 4);
INSERT INTO `user_role` (`user_id`, `role_id`) VALUES (20, 1);
INSERT INTO `user_role` (`user_id`, `role_id`) VALUES (21, 1);
INSERT INTO `user_role` (`user_id`, `role_id`) VALUES (22, 1);
INSERT INTO `user_role` (`user_id`, `role_id`) VALUES (23, 1);
INSERT INTO `user_role` (`user_id`, `role_id`) VALUES (24, 1);
INSERT INTO `user_role` (`user_id`, `role_id`) VALUES (24, 3);
INSERT INTO `user_role` (`user_id`, `role_id`) VALUES (25, 1);
INSERT INTO `user_role` (`user_id`, `role_id`) VALUES (25, 3);
INSERT INTO `user_role` (`user_id`, `role_id`) VALUES (26, 1);
COMMIT;

-- ----------------------------
-- Table structure for user_title
-- ----------------------------
DROP TABLE IF EXISTS `user_title`;
CREATE TABLE `user_title` (
  `user_id` bigint NOT NULL,
  `title_id` bigint NOT NULL,
  `gain_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`user_id`,`title_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- ----------------------------
-- Records of user_title
-- ----------------------------
BEGIN;
INSERT INTO `user_title` (`user_id`, `title_id`, `gain_time`) VALUES (1, 1, '2025-05-21 23:59:34');
INSERT INTO `user_title` (`user_id`, `title_id`, `gain_time`) VALUES (2, 1, '2025-05-21 20:07:21');
INSERT INTO `user_title` (`user_id`, `title_id`, `gain_time`) VALUES (3, 1, '2025-05-21 20:09:01');
INSERT INTO `user_title` (`user_id`, `title_id`, `gain_time`) VALUES (5, 1, '2025-05-21 20:06:24');
INSERT INTO `user_title` (`user_id`, `title_id`, `gain_time`) VALUES (7, 1, '2025-05-21 18:36:35');
INSERT INTO `user_title` (`user_id`, `title_id`, `gain_time`) VALUES (8, 1, '2025-05-21 20:06:23');
INSERT INTO `user_title` (`user_id`, `title_id`, `gain_time`) VALUES (9, 1, '2025-05-21 20:06:24');
INSERT INTO `user_title` (`user_id`, `title_id`, `gain_time`) VALUES (10, 1, '2025-05-21 20:06:24');
INSERT INTO `user_title` (`user_id`, `title_id`, `gain_time`) VALUES (11, 1, '2025-05-21 20:06:13');
INSERT INTO `user_title` (`user_id`, `title_id`, `gain_time`) VALUES (12, 1, '2025-05-21 20:06:14');
INSERT INTO `user_title` (`user_id`, `title_id`, `gain_time`) VALUES (13, 1, '2025-05-21 20:06:16');
INSERT INTO `user_title` (`user_id`, `title_id`, `gain_time`) VALUES (14, 1, '2025-05-21 20:06:16');
INSERT INTO `user_title` (`user_id`, `title_id`, `gain_time`) VALUES (16, 1, '2025-05-21 20:09:14');
INSERT INTO `user_title` (`user_id`, `title_id`, `gain_time`) VALUES (17, 1, '2025-05-21 20:06:17');
INSERT INTO `user_title` (`user_id`, `title_id`, `gain_time`) VALUES (18, 1, '2025-05-21 20:06:19');
INSERT INTO `user_title` (`user_id`, `title_id`, `gain_time`) VALUES (19, 1, '2025-05-22 13:09:48');
INSERT INTO `user_title` (`user_id`, `title_id`, `gain_time`) VALUES (19, 2, '2025-05-22 20:47:06');
INSERT INTO `user_title` (`user_id`, `title_id`, `gain_time`) VALUES (20, 1, '2025-05-21 20:06:20');
INSERT INTO `user_title` (`user_id`, `title_id`, `gain_time`) VALUES (21, 1, '2025-05-21 20:06:21');
INSERT INTO `user_title` (`user_id`, `title_id`, `gain_time`) VALUES (22, 1, '2025-05-21 20:06:21');
INSERT INTO `user_title` (`user_id`, `title_id`, `gain_time`) VALUES (23, 1, '2025-05-23 02:28:25');
INSERT INTO `user_title` (`user_id`, `title_id`, `gain_time`) VALUES (24, 1, '2025-05-23 13:04:39');
INSERT INTO `user_title` (`user_id`, `title_id`, `gain_time`) VALUES (25, 1, '2025-05-25 01:21:34');
INSERT INTO `user_title` (`user_id`, `title_id`, `gain_time`) VALUES (26, 1, '2025-05-25 14:05:28');
COMMIT;

-- ----------------------------
-- Table structure for user_tokens
-- ----------------------------
DROP TABLE IF EXISTS `user_tokens`;
CREATE TABLE `user_tokens` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `user_id` bigint NOT NULL,
  `access_token` varchar(512) NOT NULL,
  `refresh_token` varchar(512) NOT NULL,
  `expires_at` datetime NOT NULL,
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `idx_user_id` (`user_id`)
) ENGINE=InnoDB AUTO_INCREMENT=382 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- ----------------------------
-- Records of user_tokens
-- ----------------------------
BEGIN;
COMMIT;

SET FOREIGN_KEY_CHECKS = 1;
