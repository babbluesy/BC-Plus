import { app, BrowserWindow, nativeTheme } from 'electron'

try {
  if (process.platform === 'win32' && nativeTheme.shouldUseDarkColors === true) {
    require('fs').unlinkSync(require('path').join(app.getPath('userData'), 'DevTools Extensions'))
  }
} catch (_) { }

/**
 * Set `__statics` path to static files in production;
 * The reason we are setting it here is that the path needs to be evaluated at runtime
 */
if (process.env.PROD) {
  global.__statics = __dirname
}

let mainWindow
import qs from "qs";
let url=require('url')
const CLIENT_ID = "367773692104-ldevrvvo0gghju43m8cc1m6as95nkenn.apps.googleusercontent.com";
const AUTHORIZE_URI = "https://accounts.google.com/o/oauth2/v2/auth";

const queryStr = qs.stringify({
  client_id: CLIENT_ID,
  redirect_uri: "http://localhost",
  response_type: "code",
  scope: "profile email https://www.googleapis.com/auth/plus.login https://www.googleapis.com/auth/calendar",
  prompt: "consent",
  access_type : "offline",
  include_granted_scopes:true
});
const loginUrl = AUTHORIZE_URI + "?" + queryStr;


var ipcMain = require('electron').ipcMain;
ipcMain.on('asynchronous-message', function (evt, message) {
    if (message == 'createAuthWindow') {
        // Message received.
        // Create new window here.
        var authWindow = new BrowserWindow({
          width: 800,
          height: 600,
          show: false, 
          'node-integration': false,
          'web-security': false,          
        });      
        
        function handleCallback(redirecturl) {          
           console.log(redirecturl) 
           let queryData = url.parse(redirecturl, true).query;
           console.log(queryData)
           let auth_code=queryData.code;
           if(auth_code){
               console.log(auth_code,'auth_code')
               authWindow.removeAllListeners('close')
               setImmediate(() => authWindow.close())  
               evt.reply('asynchronous-reply', auth_code)
           }            
        }      

        //Handle the response from GitHub - See Update from 4/12/2015

        authWindow.webContents.on('will-navigate', function(event, url) {
          //alert("?????")
          console.log("will-navigate")
          handleCallback(url);
        });
        authWindow.webContents.on('will-redirect', function(event, url) {
          //alert("?????")
          console.log("will-redirect")
          handleCallback(url);
        });

        authWindow.webContents.on('did-get-redirect-request', function(
          event,
          oldUrl,
          newUrl
        ) {
          //alert("!!!!!")
          console.log("did-get-redirect-request")
          handleCallback(newUrl);
        });

        authWindow.loadURL(loginUrl,{userAgent: 'Chrome'});
        authWindow.show();

        // Reset the authWindow on close
        authWindow.on(
          'close',
          function() {
            authWindow = null;
          },
          false
        );

    }
});
function createWindow () {
  /**
   * Initial window options
   */
  mainWindow = new BrowserWindow({
    width: 1200,
    height: 800,
    useContentSize: true,
    webPreferences: {
      // Change from /quasar.conf.js > electron > nodeIntegration;
      // More info: https://quasar.dev/quasar-cli/developing-electron-apps/node-integration
      nodeIntegration: process.env.QUASAR_NODE_INTEGRATION,
      nodeIntegrationInWorker: process.env.QUASAR_NODE_INTEGRATION,
      allowDisplayingInsecureContent: true,
      allowRunningInsecureContent: true,
      // More info: /quasar-cli/developing-electron-apps/electron-preload-script
      // preload: path.resolve(__dirname, 'electron-preload.js')
    }
  })
  mainWindow.maximize()

  mainWindow.loadURL(process.env.APP_URL)
  
  mainWindow.on('closed', () => {
    mainWindow = null
  })
}

app.on('ready', createWindow)

app.on('window-all-closed', () => {
  if (process.platform !== 'darwin') {
    app.quit()
  }
})

app.on('activate', () => {
  if (mainWindow === null) {
    createWindow()
  }
})

