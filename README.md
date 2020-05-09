# ServerHider #

Hide servers from BungeeCord tab complete.

<form action="https://www.paypal.com/cgi-bin/webscr" method="post" target="_top">
<input type="hidden" name="cmd" value="_s-xclick" />
<input type="hidden" name="hosted_button_id" value="732AWKTTZMUNU" />
<input type="image" src="https://www.paypalobjects.com/en_GB/i/btn/btn_donate_SM.gif" border="0" name="submit" title="PayPal - The safer, easier way to pay online!" alt="Donate with PayPal button" />
<img alt="" border="0" src="https://www.paypal.com/en_GB/i/scr/pixel.gif" width="1" height="1" />
</form>

## Configuration ##

```yaml
# Whether the server names should be case sensitive
ignoreCase: true

# Whether to exclude the following servers from the list or include them
# If set to exclude, the following servers will be hidden
# If set to include, the list will be empty and only the following servers will be visible
action: "exclude"

# List of servers to hide or show
servers:
   - pvp
```