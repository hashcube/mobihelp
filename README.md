# MobiHelp plugin for GameClosure Devkit

### Configuration

Add this module to dependencies section of the manifest file.

```json
"dependencies": {
    "mobihelp": "https://github.com/hashcube/mobihelp.git#master"
}
```

Add mobihelp app key and other configurations.

```json
{
    "android": {
        "mobihelpAppKey": "xxx",
        "mobihelpAppSecret": "xxx",
        "mobihelpAutoReplyEnabled": "false",
        "mobihelpDomain": "http://foo.freshdesk.com"
    }
}
```


## Usage

`import mobihelp` on top of the file where you want to use the module.

- To open support window

`mobihelp.showSupport()`

- To send custom data, add 

`mobihelp.addCustomData('key', 'value');`

- To add bread crumbs,

`mobihelp.leaveBreadCrumb('level_90_completed')`
