server {
    server_name ipsmlittlewonders.com www.ipsmlittlewonders.com;
    client_max_body_size 50M;

    location / {
        proxy_pass http://localhost:8081;
        proxy_set_header Host $host;
        proxy_set_header X-Real-IP $remote_addr;
        proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
        proxy_set_header X-Forwarded-Proto $scheme;
    }

    listen 443 ssl;
    ssl_certificate /etc/letsencrypt/live/ipsmlittlewonders.com/fullchain.pem;
    ssl_certificate_key /etc/letsencrypt/live/ipsmlittlewonders.com/privkey.pem;
    include /etc/letsencrypt/options-ssl-nginx.conf;
    ssl_dhparam /etc/letsencrypt/ssl-dhparams.pem;
}
server {
    if ($host = ipsmlittlewonders.com) {
        return 301 https://$host$request_uri;
    }
    listen 80;
    server_name ipsmlittlewonders.com www.ipsmlittlewonders.com;
    return 404;
}
