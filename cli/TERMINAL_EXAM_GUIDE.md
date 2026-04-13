# Hướng dẫn làm bài thi Linear Training từ Terminal

## Yêu cầu

- `curl`, `jq`, `unzip`, `ssh`

---

## Bước 1 — Lấy JWT token

```bash
export TOKEN=$(curl -k -s -X POST https://<SERVER>/keycloak/realms/CRCZP/protocol/openid-connect/token \
  -d "username=<USERNAME>" \
  -d "password=<PASSWORD>" \
  -d "grant_type=password" \
  -d "client_id=CRCZP-Client" | jq -r '.access_token')
```

---

## Bước 2 — Vào bài thi

```bash
export RUN=$(curl -k -s -X POST "https://<SERVER>/training/api/v1/training-runs?accessToken=<ACCESS_TOKEN>" \
  -H "Authorization: Bearer $TOKEN")

export RUN_ID=$(echo $RUN | jq -r '.training_run_id')
export SANDBOX_ID=$(echo $RUN | jq -r '.sandbox_instance_ref_id')

echo "Training Run ID: $RUN_ID"
echo "Sandbox ID: $SANDBOX_ID"
```

Xem danh sách tất cả levels:
```bash
echo $RUN | jq '.info_about_levels[] | {title, level_type, order}'
```

Xem nội dung level hiện tại:
```bash
echo $RUN | jq '.abstract_level_dto | {title, level_type, cloud_content}'
```

---

## Bước 3 — Lấy SSH config

```bash
curl -k -s "https://<SERVER>/sandbox-service/api/v1/sandboxes/$SANDBOX_ID/user-ssh-access" \
  -H "Authorization: Bearer $TOKEN" -o ssh-config.zip

unzip ssh-config.zip -d ssh-config
cp ssh-config/*-user-key ~/.ssh/
chmod 600 ~/.ssh/*-user-key
```

---

## Bước 4 — SSH vào máy ảo sandbox

```bash
ssh -F ssh-config/*-user-config student-vm
```

---

## Bước 5 — Vượt qua ACCESS_LEVEL (nếu có)

Nếu level hiện tại là `ACCESS_LEVEL`, nhập passkey do giám thị cung cấp:

```bash
curl -k -s -X POST "https://<SERVER>/training/api/v1/training-runs/$RUN_ID/is-correct-passkey" \
  -H "Authorization: Bearer $TOKEN" \
  -H "Content-Type: application/json" \
  -d '{"passkey": "<PASSKEY>"}'
```

Chuyển sang level tiếp theo:
```bash
curl -k -s "https://<SERVER>/training/api/v1/training-runs/$RUN_ID/next-levels" \
  -H "Authorization: Bearer $TOKEN" | jq '{title, level_type}'
```

---

## Bước 6 — Nộp flag (TRAINING_LEVEL)

```bash
curl -k -s -X POST "https://<SERVER>/training/api/v1/training-runs/$RUN_ID/is-correct-answer" \
  -H "Authorization: Bearer $TOKEN" \
  -H "Content-Type: application/json" \
  -d '{"answer": "<FLAG>"}'
```

Response `"correct": true` nghĩa là đúng.

---

## Bước 7 — Chuyển sang level tiếp theo

```bash
curl -k -s "https://<SERVER>/training/api/v1/training-runs/$RUN_ID/next-levels" \
  -H "Authorization: Bearer $TOKEN" | jq '{title, level_type}'
```

---

## Bước 8 — Kết thúc bài thi

```bash
curl -k -s -X PUT "https://<SERVER>/training/api/v1/training-runs/$RUN_ID" \
  -H "Authorization: Bearer $TOKEN"
```

Không có output = thành công (HTTP 200).

---

## Bước 9 — Dọn dẹp

```bash
rm -f ~/.ssh/*-user-key
rm -rf ~/ssh-config ~/ssh-config.zip
```

---

## Lấy deep link để mở giao diện web (tùy chọn)

```bash
curl -k -s -X POST "https://<SERVER>/training/api/v1/training-runs/$RUN_ID/deep-link" \
  -H "Authorization: Bearer $TOKEN" | jq -r '.deep_link_url'
```
