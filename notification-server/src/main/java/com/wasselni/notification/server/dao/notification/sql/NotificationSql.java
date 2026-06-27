
package com.wasselni.notification.server.dao.notification.sql;

public class NotificationSql {

	public static final String UPDATE_EMAIL_NOTIFICATION = "UPDATE                                       \n"
			+ "        notifications                        \n" + "    SET                                      \n"
			+ "        notify_email_flag = TRUE             \n" + "        ,date_sent = CURRENT_TIMESTAMP       \n"
			+ "        ,success = ?                         \n" + "        ,error_message = ?                   \n"
			+ "    WHERE                                    \n" + "        notification_id = ?                  \n";

	public static final String UPDATE_SMS_NOTIFICATION = "UPDATE                                       \n"
			+ "        notifications                        \n" + "    SET                                      \n"
			+ "        notify_sms_flag = TRUE               \n" + "        ,date_sent = CURRENT_TIMESTAMP       \n"
			+ "        ,success = ?                         \n" + "        ,error_message = ?                   \n"
			+ "    WHERE                                    \n" + "        notification_id = ?                  \n";

	public static final String UPDATE_WHATSAPP_NOTIFICATION = "UPDATE                                       \n"
			+ "        notifications                        \n" + "    SET                                      \n"
			+ "        notify_whatsapp_flag = TRUE          \n" + "        ,date_sent = CURRENT_TIMESTAMP       \n"
			+ "        ,success = ?                         \n" + "        ,error_message = ?                   \n"
			+ "    WHERE                                    \n" + "        notification_id = ?                  \n";

	public static final String GET_NOTIFICATION_LIST = "SELECT                                              \n"
			+ "        notification_id                             \n"
			+ "        ,recipient                                  \n"
			+ "        ,subject                                    \n"
			+ "        ,text                                       \n"
			+ "        ,calling_code                               \n"
			+ "        ,mobile_number                              \n"
			+ "        ,notify_email                               \n"
			+ "        ,notify_sms                                 \n"
			+ "        ,notify_whatsapp                            \n"
			+ "    FROM                                            \n"
			+ "        notifications                               \n"
			+ "    WHERE                                           \n"
			+ "        (                                           \n"
			+ "            notify_email = TRUE                     \n"
			+ "            AND notify_email_flag = FALSE           \n"
			+ "        )                                           \n"
			+ "        OR (                                        \n"
			+ "            notify_sms = TRUE                       \n"
			+ "            AND notify_sms_flag = FALSE             \n"
			+ "        )                                           \n"
			+ "        OR (                                        \n"
			+ "            notify_whatsapp = TRUE                  \n"
			+ "            AND notify_whatsapp_flag = FALSE        \n"
			+ "        )                                           \n";

	public static final String ADD_LOG_EMAIL = "INSERT                         \n" + "    INTO                       \n"
			+ "        notifications (        \n" + "            recipient          \n"
			+ "            ,subject           \n" + "            ,text              \n"
			+ "            ,notify_email      \n" + "            ,notify_sms        \n"
			+ "            ,calling_code      \n" + "            ,mobile_number     \n"
			+ "        )                      \n" + "    VALUES (                   \n"
			+ "        ?                      \n" + "        ,?                     \n"
			+ "        ,?                     \n" + "        ,?                     \n"
			+ "        ,?                     \n" + "        ,?                     \n"
			+ "        ,?                     \n" + "    )                          \n";

}
