using System.Web;

namespace DolarYa.Backoffice.Common
{
    public class SessionHandler
    {
        private const string KeyUserId = "UserId";
        private const string KeyUsername = "Username";

        public static string UserId
        {
            get
            {
                try
                {
                    return HttpContext.Current.Session[KeyUserId].ToString();
                }
                catch
                {
                    return null;
                }
            }
            set
            {
                HttpContext.Current.Session[KeyUserId] = value;
            }
        }

        public static string Username
        {
            get
            {
                try
                {
                    return HttpContext.Current.Session[KeyUsername].ToString();
                }
                catch
                {
                    return null;
                }
            }
            set
            {
                HttpContext.Current.Session[KeyUsername] = value;
            }
        }

        public static bool IsUserLogged
        {
            get
            {
                return !string.IsNullOrWhiteSpace(UserId);
            }
        }

        public static void SetUserId(HttpContext current, string userId)
        {
            current.Session[KeyUserId] = userId;
        }
    }
}
